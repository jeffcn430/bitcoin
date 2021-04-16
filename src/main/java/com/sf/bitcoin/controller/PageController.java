package com.sf.bitcoin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Administrator
 */
@Controller
public class PageController {
    @GetMapping("offset-list")
    public ModelAndView offsetList(ModelAndView model) {
        model.setViewName("offset-list");
        return model;
    }

    /**
     * 首页
     *
     * @param model
     * @return
     */
    @GetMapping("/")
    public ModelAndView index(ModelAndView model) {
        model.setViewName("index");
        return model;
    }

    /**
     * 主控页面
     *
     * @param model
     * @return
     */
    @GetMapping("welcome")
    public ModelAndView welcome(ModelAndView model) {
        model.setViewName("welcome");
        return model;
    }
}
