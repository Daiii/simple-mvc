package cn.self.zhangbo.controller;

import cn.self.zhangbo.kernel.annotation.*;
import cn.self.zhangbo.pojo.User;
import cn.self.zhangbo.service.IndexService;

@Controller
public class IndexController {

    @Autowired
    IndexService indexService;

    @RequestMapping("/index")
    public String index() {
        return "redirect:success";
    }

    @RequestMapping("/login")
    public String login() {
        return "forward:index";
    }

    @RequestMapping("/default")
    public String normal() {
        return "default";
    }

    @RequestMapping("/sayHello")
    @ResponseBody
    public User sayHello(@RequestParam("name") String name) {
        return new User(name);
    }
}
