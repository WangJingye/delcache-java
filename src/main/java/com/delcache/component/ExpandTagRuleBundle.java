package com.delcache.component;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.content.tagrules.TagRuleBundle;
import org.sitemesh.content.tagrules.html.ExportTagToContentRule;
import org.sitemesh.tagprocessor.State;

public class ExpandTagRuleBundle implements TagRuleBundle {
    @Override
    public void install(State state, ContentProperty contentProperty, SiteMeshContext siteMeshContext) {
        state.addRule("custom_script", new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("custom_script"), false));
        state.addRule("custom_css", new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("custom_css"), false));
    }

    @Override
    public void cleanUp(State state, ContentProperty contentProperty, SiteMeshContext siteMeshContext) {

    }
}
