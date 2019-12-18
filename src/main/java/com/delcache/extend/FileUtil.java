package com.delcache.extend;

import java.io.*;

public class FileUtil {

    /**
     * 创建文件
     * @param pathName
     * @return
     */
    public Boolean createFile(String pathName) {
        try {
            File file = new File(pathName);
            String parentPath = file.getParent();
            File dir = new File(parentPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception exception) {
            return false;
        }
        return true;
    }


    /**
     * 删除文件
     * @param pathName
     * @return
     */
    public Boolean deleteFile(String pathName) {
        try {
            File file = new File(pathName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    /**
     * 读取文件内容
     * @param pathName
     * @return
     */
    public String readFile(String pathName) {
        StringBuilder fileValue = new StringBuilder();
        try {

            String encoding = "UTF-8";
            File file = new File(pathName);

            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    fileValue.append(lineTxt).append("\n");
                }
                read.close();
            }
        } catch (Exception exception) {
            return "";
        }
        return fileValue.toString();
    }

    /**
     * 追加
     * @param pathName
     * @param content
     * @return
     */
    public Boolean appendFile(String pathName, String content) {
        try {
            this.createFile(pathName);
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(pathName, true), "UTF-8");
            fw.write(content);
            fw.flush();
            fw.close();
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    /**
     * 获取扩展名
     * @param pathName
     * @return
     */
    public String ext(String pathName) {
        return pathName.substring(pathName.lastIndexOf(".") + 1);
    }

}