package com.hx199513.community.community.cache;

import com.hx199513.community.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {
    public static List<TagDTO> get() {
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("java", "c", "c++", "php", "perl", "python", "javascript", "c#", "ruby", "go", "lua", "node.js", "erlang", "scala", "bash", "actionscript", "golang", "typescript", "flutter"));
        tagDTOS.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("laravel", "spring", "express", "django", "flask", "yii", "ruby-on-rails", "tornado", "koa", "struts"));
        tagDTOS.add(framework);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("vim", "emacs", "ide", "eclipse", "xcode", "intellij-idea", "textmate", "sublime-text", "visual-studio", "git", "github", "svn", "hg", "docker", "ci"));
        tagDTOS.add(tool);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("linux", "unix", "ubuntu", "windows-server", "centos", "负载均衡", "缓存", "apache", "nginx"));
        tagDTOS.add(server);

        TagDTO dataBase = new TagDTO();
        dataBase.setCategoryName("数据库");
        dataBase.setTags(Arrays.asList("数据库","mysql","sqlite","oracle","sql","nosql","redis","mongodb","memcached","postgresql"));
        tagDTOS.add(dataBase);

        TagDTO javaScript = new TagDTO();
        javaScript.setCategoryName("JavaScript开发");
        javaScript.setTags(Arrays.asList("javascript","jquery","node.js","chrome","firefox","internet-explorer","angular.js","typescript","ecmascript","vue.js","react.js"));
        tagDTOS.add(javaScript);

        return tagDTOS;
    }
    public static String filterInvalid(String tags){
        String[] splits= StringUtils.split(tags,",");
        List<TagDTO> tagDTOS= get();

        List<String> tagList=  tagDTOS.stream().flatMap(tag-> tag.getTags().stream()).collect(Collectors.toList());
        String invalid=Arrays.stream(splits).filter(t->!tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
