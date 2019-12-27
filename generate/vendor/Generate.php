<?php


class Generate extends ObjectAccess
{
    public static $instance;
    public $model;
    public $basePath;
    public $controllerUrl;
    public $module;
    public $theme;
    public $primaryKey;
    public $option;
    public $field = [];
    public $app;
    public $columnTypes;
    public $uniqueColumns = [];
    public $templatePath = 'view/';

    /**
     * Generate constructor.
     * @param $app
     * @param $module
     * @param $table
     * @throws \Exception
     */
    public function __construct($option)
    {
        $this->theme = $option['template'];
        $this->basePath = $option['path'];
        $this->app = $this->theme == 'api' ? 'api' : 'backend';
        $model = '';
        $arr = explode('_', $option['table']);
        foreach ($arr as $v) {
            $v = trim($v, '_');
            if ($v == '') {
                continue;
            }
            $model .= ucfirst($v);
        }
        $this->model = $model;

        $controller = '';
        for ($i = 0; $i < strlen($this->model); $i++) {
            $new = $this->model[$i];
            $char = ord($new);
            //是大写字母
            if ($char >= 65 && $char <= 90) {
                $new = '-' . chr($char + 32);
            }
            $controller .= $new;
        }
        $this->module = $option['module'];
        $this->controller = trim($controller, '-');
        $keys = \Db::table($this->model)->getKeys();
        $this->option = $option;
        $uniqueColumns = [];
        foreach ($keys as $v) {
            //主键单独处理
            if ($v['Key_name'] == 'PRIMARY') {
                $this->primaryKey = $v['Column_name'];
                continue;
            }
            //不允许重复
            if ($v['Non_unique'] == 0) {
                $uniqueColumns[$v['Key_name']][] = $v['Column_name'];
            }
        }
        $columnTypes = [];
        $fields = \Db::table($this->model)->getFields();
        $this->field = $fields;
        foreach ($fields as $field => $v) {
            $type = [];
            if (strpos($v['Type'], 'int') !== false) {
                $type = 'int';
            } elseif (strpos($v['Type'], 'char') !== false || strpos($v['Type'], 'ext') !== false) {
                $type = 'String';
            } elseif (strpos($v['Type'], 'float') !== false ||
                strpos($v['Type'], 'float') !== false ||
                strpos($v['Type'], 'double') !== false ||
                strpos($v['Type'], 'decimal') !== false
            ) {
                $type = 'Double';
            }
            $columnTypes[$field] = $type;
        }
        $this->columnTypes = $columnTypes;
        $this->uniqueColumns = $uniqueColumns;
    }

    /**
     * @param $app
     * @param $module
     * @return Generate
     * @throws \Exception
     */
    public static function instance($option)
    {
        if (is_null(self::$instance)) {
            self::$instance = new static($option);
        }
        return self::$instance;
    }

    public function run()
    {
        if ($this->theme == 'web') {
            $this->common()->entity()->module()->service()->controller()->view()->js();
        } else {
            $this->common()->entity()->module()->service()->controller();
        }
    }

    public function js()
    {
        $dir = $this->basePath . '/src/main/webapp/static/js/' . $this->getKeyRoute($this->module);
        if (!file_exists($dir)) {
            mkdir($dir, 0755, true);
        }
        //js
        $rules = '';
        $rulesMessage = '';
        foreach ($this->option['fcomment'] as $field => $label) {
            if ($field == $this->primaryKey) {
                continue;
            }
            if (!isset($this->option['frequire'][$field]) || $this->option['frequire'][$field] == 0) {
                continue;
            }
            if ($rules) {
                $rules .= ',';
                $rulesMessage .= ',';
            }
            $rules .= PHP_EOL . '            ' . $field . ': {';
            $rulesMessage .= PHP_EOL . '            ' . $field . ': {';
            $rules .= PHP_EOL . '                required: true';
            $rulesMessage .= PHP_EOL . '                required: \'请输入' . $label . '\'';
            $rules .= PHP_EOL . '            }';
            $rulesMessage .= PHP_EOL . '            }';

        }
        $statusJs = '';
        if (isset($this->option['fcomment']['status']) && $this->option['fchoice']['status'] == 1 && count(($statusList = $this->getChooseList('status')['list'])) == 2) {
            $statusPartJs = '';
            foreach ($statusList as $k => $v) {
                $statusPartJs .= '
                if (args.status == ' . $k . ') {
                    data = {
                        \'btn_class\': \'' . ($k == 0 ? 'btn-success' : 'btn-danger') . '\',
                        \'class_name\': \'' . ($k == 0 ? 'glyphicon-ok-circle' : 'glyphicon-remove-circle') . '\',
                        \'status\': \'' . ($k == 0 ? 1 : 0) . '\',
                        \'name\': \'' . ($k == 0 ? '启用' : '禁用') . '\',
                        \'title\': \'' . $v . '\',
                    };
                }';
            }
            $statusJs = PHP_EOL . '    $(\'.set-status-btn\').click(function () {
        let $this = $(this);
        let tr = $(this).parents(\'tr\');
        let args = {
            id: $this.data(\'id\'),
            status: $this.data(\'status\')
        };
        POST($this.data(\'url\'), args, function (res) {
            if (res.code == 200) {
                $.success(res.message);' . $statusPartJs . '
                tr.find(\'.status\').html(data.title);
                $this.data(\'status\', data.status);
                $this.removeClass(\'btn-success\').removeClass(\'btn-danger\').addClass(data.btn_class);
                $this.find(\'.glyphicon\').removeClass(\'glyphicon-remove-circle\').removeClass(\'glyphicon-ok-circle\').addClass(data.class_name);
                $this.find(\'span\').html(data.name);
            } else {
                $.error(res.message);
            }
        }, \'json\');
    });';
        }
        $filename = $dir . '/' . $this->getKeyRoute($this->model) . '.js';
        $file = $this->templatePath . 'js';
        $str = file_get_contents($file);
        $str = str_replace('{{model}}', $this->model, $str);
        $str = str_replace('{{app}}', $this->app, $str);
        $str = str_replace('{{module}}', $this->module, $str);
        $str = str_replace('{{rules}}', $rules, $str);
        $str = str_replace('{{rulesMessage}}', $rulesMessage, $str);
        $str = str_replace('{{statusJs}}', $statusJs, $str);
        $str = str_replace('{{primaryKey}}', $this->primaryKey, $str);

        if (!file_exists($filename)) {
            file_put_contents($filename, $str);
        }
        return $this;
    }

    public function view()
    {
        $dir = $this->basePath . '/src/main/webapp/html/' . $this->getKeyRoute($this->module) . '/' . $this->getKeyRoute($this->model);
        if (!file_exists($dir)) {
            mkdir($dir, 0755, true);
        }
        //edit
        $inputAssign = [];
        $inputParams = '';
        foreach ($this->option['fcomment'] as $field => $label) {
            if ($field == $this->primaryKey) {
                continue;
            }
            if (!isset($this->option['feditshow'][$field]) || $this->option['feditshow'][$field] == 0) {
                continue;
            }
            $inputParams .= PHP_EOL . '    <div class="form-group">';
            $inputParams .= PHP_EOL . '        <label class="col-sm-2 control-label">' . $label . '</label>';
            $inputParams .= PHP_EOL . '        <div class="col-sm-10">';
            if (in_array($this->option['ftype'][$field], ['select', 'select2', 'radio', 'checkbox'])) {
                $res = $this->getChooseList($field);
            }
            if (in_array($this->option['ftype'][$field], ['select', 'select2', 'radio', 'checkbox'])) {
                $inputAssign[] = '    Map<String, String> ' . $res['variable'] . ' = (Map<String, String>) request.getAttribute("' . $res['variable'] . '");';
                $inputParams .= PHP_EOL . '            <%= SelectInput.show(' . $res['variable'] . ', data != null ? String.valueOf(data.get' . $this->getKeyAction($field) . '()) : "", "' . $field . '", "' . $this->option['ftype'][$field] . '") %>';
            } else if ($this->option['ftype'][$field] == 'textarea') {
                $inputParams .= PHP_EOL . '            <textarea name="' . $field . '" class="form-control" placeholder="请输入' . $label . '">${data.' . $this->getKeyAction1($field) . '}</textarea>';
            } else if ($this->option['ftype'][$field] == 'image') {
                $inputParams .= PHP_EOL . '            <%= ImageInput.show(data != null ? data.get' . $this->getKeyAction($field) . '() : "", "' . $field . '")%>';
            } else {
                $placeholder = '请输入' . $label;
                if (in_array($this->option['ftype'][$field], ['date', 'date-normal', 'datetime', 'datetime-normal'])) {
                    $placeholder = $label . '，格式为2019-01-01';
                } else if (in_array($this->option['ftype'][$field], ['datetime', 'datetime-normal'])) {
                    $placeholder = $label . '，格式为2019-01-01 09:00:00';
                }
                $inputParams .= PHP_EOL . '            <input type="text" name="' . $field . '" class="form-control" value="${data.' . $this->getKeyAction1($field) . '}" placeholder="' . $placeholder . '">';
            }
            $inputParams .= PHP_EOL . '        </div>';
            $inputParams .= PHP_EOL . '    </div>';
        }
        $filename = $dir . '/edit.jsp';
        $file = $this->templatePath . '/edit.jsp';
        $str = file_get_contents($file);
        $str = str_replace('{{model}}', $this->model, $str);
        $str = str_replace('{{modelRoute}}', $this->getKeyRoute($this->model), $str);
        $str = str_replace('{{app}}', $this->app, $str);
        $str = str_replace('{{module}}', $this->module, $str);
        $str = str_replace('{{inputParams}}', $inputParams, $str);
        $str = str_replace('{{primaryKey}}', $this->primaryKey, $str);
        $str = str_replace('{{u_primaryKey}}', $this->getKeyRoute($this->primaryKey), $str);

        if (!file_exists($filename)) {
            file_put_contents($filename, $str);
        }
        //index
        $searchs = [];
        $searchPer = '';
        $header = '';
        $body = '';
        $imports = [];
        foreach ($this->option['fcomment'] as $field => $label) {
            $res = $this->getChooseList($field);
            if (isset($this->option['fpageshow'][$field]) && $this->option['fpageshow'][$field] == 1) {
                $header .= PHP_EOL . '            <th>' . $label . '</th>';
                $statusHtml = '';
                if ($field == 'status' && $this->option['fchoice'][$field] == 1 && count($res['list']) == 2) {
                    $statusHtml = ' class="status"';
                }
                if (in_array($this->option['ftype'][$field], ['select', 'radio', 'checkbox'])) {
                    $body .= PHP_EOL . '                <td' . $statusHtml . '><%= ' . $res['variable'] . '.get(v.get' . $this->getKeyAction($field) . '()) %></td>';
                } else if ($this->option['ftype'][$field] == 'date') {
                    $imports[] = PHP_EOL . '<%@ page import="com.delcache.extend.Util" %>';
                    $body .= PHP_EOL . '                <td' . $statusHtml . '><%= Util.stampToDate(v.get' . $this->getKeyAction($field) . '(),"yyyy-MM-dd") %></td>';
                } else if ($this->option['ftype'][$field] == 'datetime') {
                    $imports[] = PHP_EOL . '<%@ page import="com.delcache.extend.Util" %>';
                    $body .= PHP_EOL . '                <td' . $statusHtml . '><%= Util.stampToDate(v.get' . $this->getKeyAction($field) . '()) %></td>';
                } else if ($this->option['ftype'][$field] == 'image') {
                    $body .= PHP_EOL . '                <td' . $statusHtml . '>';
                    $body .= PHP_EOL . '                    <% if (!v.get' . $this->getKeyAction($field) . '().isEmpty()){ %>';
                    $body .= PHP_EOL . '                        <img src="<%= v.get' . $this->getKeyAction($field) . '() %>" style="width: 60px;height: 60px;">';
                    $body .= PHP_EOL . '                    <% } %>';
                    $body .= PHP_EOL . '                </td' . $statusHtml . '>';
                } else {
                    $body .= PHP_EOL . '                <td' . $statusHtml . '><%= v.get' . $this->getKeyAction($field) . '() %></td>';
                }
            }
            if (isset($this->option['fpagesearch1'][$field]) && $this->option['fpagesearch1'][$field] == 1) {
                $searchPer .= PHP_EOL . '    <div class="form-content">';
                $searchPer .= PHP_EOL . '        <span class="col-form-label search-label">' . $label . '</span>';
                if (!in_array($this->option['ftype'][$field], ['select', 'select2', 'radio', 'checkbox'])) {
                    $searchPer .= PHP_EOL . '        <input class="form-control search-input" name="' . $field . '" value="${params.' . $field . '}">';
                } else {
                    $isSelect2 = $this->option['ftype'][$field] == 'select2' ? ' select2' : '';
                    $searchPer .= PHP_EOL . '        <select class="form-control search-input' . $isSelect2 . '" name="' . $field . '">';
                    $searchPer .= PHP_EOL . '            <option value="">请选择</option>';
                    $searchPer .= PHP_EOL . '           <%';
                    $searchPer .= PHP_EOL . '           Map<String, String>  ' . $res['variable'] . ' = (Map<String, String>) request.getAttribute("' . $res['variable'] . '");';
                    $searchPer .= PHP_EOL . '           for (Map.Entry<String, String> entry : ' . $res['variable'] . '.entrySet()) {%>';
                    $searchPer .= PHP_EOL . '                <option value="<%= entry.getKey() %>" <%= entry.getKey().equals(params.get("' . $field . '")) ? "selected" : "" %>><%= entry.getValue() %></option>';
                    $searchPer .= PHP_EOL . '           <% }%>';
                    $searchPer .= PHP_EOL . '        </select>';
                }
                $searchPer .= PHP_EOL . '    </div>';
            }
            if (isset($this->option['fpagesearch2'][$field]) && $this->option['fpagesearch2'][$field] == 1) {
                $searchs[] = PHP_EOL . '                    put("' . $field . '", "' . $label . '");';
            }
        }
        $header .= PHP_EOL . '            <th>操作</th>';
        $imports = array_values(array_unique($imports));
        $searchList = implode('', $searchs);
        $statusIndex = '';
        if (isset($this->option['fcomment']['status']) && $this->option['fchoice']['status'] == 1 && count(($statusList = $this->getChooseList('status')['list'])) == 2) {
            $statusIndex = PHP_EOL . '                    <% if (v.getStatus() == 1) {%>
                <div class="btn btn-danger btn-sm set-status-btn" data-id="<%= v.get' . $this->getKeyAction($this->primaryKey) . '() %>" data-status="0"
                     data-url="<%= UrlManager.createUrl("/' . $this->module . '/' . $this->getKeyRoute($this->model) . '/set-status") %>">
                    <i class="glyphicon glyphicon-ban-circle"></i> 禁用
                </div>
                <% } else { %>
                <div class="btn btn-success btn-sm set-status-btn" data-id="<%= v.get' . $this->getKeyAction($this->primaryKey) . '() %>" data-status="1"
                     data-url="<%= UrlManager.createUrl("/' . $this->module . '/' . $this->getKeyRoute($this->model) . '/set-status") %>">
                    <i class="glyphicon glyphicon-ok-circle"></i> 启用
                </div>
                <% }%>';
        }

        $filename = $dir . '/index.jsp';
        $file = $this->templatePath . '/index.jsp';
        $str = file_get_contents($file);
        $str = str_replace('{{model}}', $this->model, $str);
        $str = str_replace('{{imports}}', implode(PHP_EOL, $imports), $str);
        $str = str_replace('{{modelRoute}}', $this->getKeyRoute($this->model), $str);
        $str = str_replace('{{app}}', $this->app, $str);
        $str = str_replace('{{module}}', $this->module, $str);
        $str = str_replace('{{searchPer}}', $searchPer, $str);
        $str = str_replace('{{searchParams}}', $searchList, $str);
        $str = str_replace('{{table-header}}', $header, $str);
        $str = str_replace('{{table-body}}', $body, $str);
        $str = str_replace('{{statusIndex}}', $statusIndex, $str);
        $str = str_replace('{{primaryKey}}', $this->primaryKey, $str);
        $str = str_replace('{{u_PrimaryKey}}', $this->getKeyAction($this->primaryKey), $str);

        if (!file_exists($filename)) {
            file_put_contents($filename, $str);
        }
        return $this;
    }

    public function getChooseList($field)
    {
        if (isset($this->option['fchoice'][$field])) {
            if ($this->option['fchoice'][$field] == 1) {
                $list = $this->option['fchoicelist'][$field] ? explode(',', $this->option['fchoicelist'][$field]) : [];
                $res = [];
                $var = $list[0];
                unset($list[0]);
                foreach ($list as $v) {
                    $arr = explode(':', $v);
                    $res[$arr[0]] = $arr[1];
                }
                return ['variable' => $var, 'list' => $res, 'type' => 1];
            } else {
                $arr = $this->option['fchoicelist'][$field] ? explode(':', $this->option['fchoicelist'][$field]) : [];
                $res = [
                    'type' => 2,
                    'variable' => $arr[3],
                    'table' => $arr[0],
                    'key' => $arr[1],
                    'value' => $arr[2],
                    'where' => $arr[2],
                ];
                return $res;
            }
        }
    }

    public function controller()
    {
        $dir = $this->basePath . '/src/main/java/com/delcache/' . $this->app . '/' . $this->getKeyRoute($this->module) . '/controller';
        if (!file_exists($dir)) {
            mkdir($dir, 0755, true);
        }
        $filename = $dir . '/' . $this->model . 'Controller.java';
        $file = $this->templatePath . 'controller';
        $str = file_get_contents($file);
        $otherAssign = '';
        $parseFile = '';
        $otherDefineService = '';
        $importAssign = [];
        foreach ($this->option['fcomment'] as $field => $label) {
            if (in_array($this->option['ftype'][$field], ['select', 'select2', 'radio', 'checkbox'])) {
                $res = $this->getChooseList($field);
                if ($res['type'] == 1) {
                    $otherDefineService .= PHP_EOL . '    private Map<String, String> ' . $res['variable'] . ' = new LinkedHashMap<String, String>() {';
                    $otherDefineService .= PHP_EOL . '        {';
                    foreach ($res['list'] as $key => $v) {
                        $otherDefineService .= PHP_EOL . '            put("' . $key . '", "' . $v . '");';
                    }
                    $otherDefineService .= PHP_EOL . '        }';
                    $otherDefineService .= PHP_EOL . '    };';
                    $otherAssign .= PHP_EOL . '        model.addAttribute("' . $res['variable'] . '", this.' . $res['variable'] . ');';
                } else {
                    $importAssign[] = 'import com.delcache.extend.Util;';
                    $otherAssign .= PHP_EOL . '        Map<String, String> ' . $res['variable'] . ' = Util.arrayColumn((List<' . $res['table'] . '>) Db.table(' . $res['table'] . '.class).findAll(), "' . $res['value'] . '", "' . $res['key'] . '");';
                    $otherAssign .= PHP_EOL . '        model.addAttribute("' . $res['variable'] . '", ' . $res['variable'] . ');';
                }
            } else if ($this->option['ftype'][$field] == 'image') {
                $parseFile .= PHP_EOL . '              String ' . $field . ' = Request.getInstance(request).parseFileAndParams("' . $field . '", "upload/' . $this->model . '");';
                $parseFile .= PHP_EOL . '              params.put("' . $field . '", ' . $field . ');';
            }
        }
        $statusAction = '';
        if (isset($this->option['fcomment']['status']) && $this->option['fchoice']['status'] == 1 && count(($statusList = $this->getChooseList('status')['list'])) == 2) {
            $statusAction = PHP_EOL . '
    @ResponseBody
    @RequestMapping(value = "' . $this->module . '/' . $this->getKeyRoute($this->model) . '/set-status", method = RequestMethod.POST)
    public Object setStatus(HttpServletRequest request) throws Exception {
        Map<String, Object> params = Request.getInstance(request).getParams();
        if (Util.parseInt(params.get("id")) == 0) {
            throw new Exception("参数有误");
        }
        Db.table(' . $this->model . '.class).where("' . $this->primaryKey . '", params.get("id")).update("status", params.get("status"));
        return this.success("操作成功");
    }';
        }
        $str = str_replace('{{model}}', $this->model, $str);
        $str = str_replace('{{modelRoute}}', $this->getKeyRoute($this->model), $str);
        $str = str_replace('{{otherDefineService}}', $otherDefineService, $str);
        $str = str_replace('{{otherAssign}}', $otherAssign, $str);
        $str = str_replace('{{importAssign}}', count($importAssign) ? implode(PHP_EOL, $importAssign) : '', $str);
        $str = str_replace('{{parseFile}}', $parseFile, $str);
        $str = str_replace('{{app}}', $this->app, $str);
        $str = str_replace('{{module}}', $this->module, $str);
        $str = str_replace('{{primaryKey}}', $this->primaryKey, $str);
        $str = str_replace('{{name}}', $this->option['name'], $str);
        $str = str_replace('{{statusAction}}', $statusAction, $str);
        $str = str_replace('{{u_primaryKey}}', $this->getKeyAction($this->primaryKey), $str);
        if (!file_exists($filename)) {
            file_put_contents($filename, $str);
        }
        return $this;
    }

    public function service()
    {
        $dir = $this->basePath . '/src/main/java/com/delcache/' . $this->app . '/' . $this->module . '/service';
        if (!file_exists($dir)) {
            mkdir($dir, 0755, true);
        }

        $filename = $dir . '/' . $this->model . 'Service.java';
        $file = $this->templatePath . 'service';
        $str = file_get_contents($file);
        $selectorParams = '';
        foreach ($this->option['fcomment'] as $field => $label) {
            $selectorParams .= PHP_EOL . '        if (!StringUtils.isEmpty(params.get("' . $field . '"))) {';
            $selectorParams .= PHP_EOL . '            selector.where("' . $field . '", params.get("' . $field . '"), "like");';
            $selectorParams .= PHP_EOL . '        }';
        }

        $str = str_replace('{{selectorParams}}', $selectorParams, $str);
        $str = str_replace('{{model}}', $this->model, $str);
        $str = str_replace('{{modelRoute}}', $this->getKeyRoute($this->model), $str);
        $str = str_replace('{{app}}', $this->app, $str);
        $str = str_replace('{{module}}', $this->module, $str);
        if (!file_exists($filename)) {
            file_put_contents($filename, $str);
        }
        return $this;
    }

    public function module()
    {
        $dir = $this->basePath . '/src/main/java/com/delcache/' . $this->app . '/' . $this->module;
        if (!file_exists($dir)) {
            mkdir($dir, 0755, true);
        }
        if ($this->app == 'backend') {
            $dirList = [
                'service', 'controller'
            ];
        }
        foreach ($dirList as $v) {
            if (!file_exists($dir . '/' . $v)) {
                mkdir($dir . '/' . $v, 0755, true);
            }
        }
        //todo Config暂时不生成
        return $this;
    }

    public function common()
    {
        $dir = $this->basePath . '/src/main/java/com/delcache/' . $this->app . '/common';
        if (!file_exists($dir)) {
            mkdir($dir, 0755, true);
        }
        $dirList = [
            'config'
        ];
        foreach ($dirList as $v) {
            if (!file_exists($dir . '/' . $v)) {
                mkdir($dir . '/' . $v, 0755, true);
            }
        }
        $filename = $dir . '/BaseService.java';
        if (!file_exists($filename)) {
            $file = $this->templatePath . 'base_service';
            $str = file_get_contents($file);
            $str = str_replace('{{app}}', $this->app, $str);
            file_put_contents($filename, $str);
        }
        $filename = $dir . '/BaseController.java';
        if (!file_exists($filename)) {
            $file = $this->templatePath . 'base_controller';
            $str = file_get_contents($file);
            $str = str_replace('{{app}}', $this->app, $str);
            file_put_contents($filename, $str);
        }

        $filename = $dir . '/config/Config.java';
        if (!file_exists($filename)) {
            $file = $this->templatePath . 'common_config';
            $str = file_get_contents($file);
            $str = str_replace('{{app}}', $this->app, $str);
            file_put_contents($filename, $str);
        }

        return $this;
    }

    public function entity()
    {
        $dir = $this->basePath . '/src/main/java/com/delcache/common/entity';
        if (!file_exists($dir)) {
            mkdir($dir, 0755, true);
        }
        $declare = '';
        $stgt = '';
        foreach ($this->columnTypes as $field => $type) {
            $stgt .= PHP_EOL . PHP_EOL . '    public ' . $type . ' get' . $this->getKeyAction($field) . '() {';
            $stgt .= PHP_EOL . '        return ' . $this->getKeyAction1($field) . ';';
            $stgt .= PHP_EOL . '    }';
            $stgt .= PHP_EOL . PHP_EOL . '    public void set' . $this->getKeyAction($field) . '(' . $type . ' ' . $this->getKeyAction1($field) . ') {';
            $stgt .= PHP_EOL . '        this.' . $this->getKeyAction1($field) . ' = ' . $this->getKeyAction1($field) . ';';
            $stgt .= PHP_EOL . '    }';
            if ($field == $this->primaryKey) {
                continue;
            }
            $declare .= PHP_EOL .PHP_EOL . '    private ' . $type . ' ' . $this->getKeyAction1($field) . ';';

        }
        $filename = $dir . '/' . $this->model . '.java';
        $file = $this->templatePath . 'entity';
        $str = file_get_contents($file);
        $str = str_replace('{{declare}}', $declare, $str);
        $str = str_replace('{{model}}', $this->model, $str);
        $str = str_replace('{{primaryKey}}', $this->primaryKey, $str);
        $str = str_replace('{{table}}', $this->getTable($this->model), $str);
        $str = str_replace('{{setter-getter}}', $stgt, $str);
        file_put_contents($filename, $str);
        return $this;
    }

    protected function getKeyAction($str)
    {
        $list = explode('_', $str);
        $res = [];
        foreach ($list as $v) {
            $res[] = ucfirst(strtolower($v));
        }
        return implode('', $res);
    }

    protected function getKeyAction1($str)
    {
        $list = explode('_', $str);
        $res = [];
        foreach ($list as $i => $v) {
            if ($i == 0) {
                $res[] = strtolower($v);
            } else {
                $res[] = ucfirst(strtolower($v));

            }
        }
        return implode('', $res);
    }

    protected function getKeyRoute($str)
    {
        if (strpos($str, '_') === false) {
            $str = trim(preg_replace('/([A-Z])/', '_$1', $str), '_');
        }
        $list = explode('_', $str);
        $res = [];
        foreach ($list as $v) {
            $res[] = strtolower($v);
        }
        return implode('-', $res);
    }

    protected function getTable($str)
    {
        if (strpos($str, '_') === false) {
            $str = trim(preg_replace('/([A-Z])/', '_$1', $str), '_');
        }
        $list = explode('_', $str);
        $res = [];
        foreach ($list as $v) {
            $res[] = strtolower($v);
        }
        return 'tbl_' . implode('_', $res);
    }
}