package com.abc.p2p.web;

import com.abc.p2p.service.BidInfoService;
import com.abc.p2p.service.UserService;
import com.abc.p2p.service.LoanService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author yang
 * @Date 2020/10/10 21:22
 * @Description : 首页控制层
 */
@Controller
public class IndexController {

    @Reference(interfaceClass = LoanService.class, timeout = 20000, version = "1.0.0")
    LoanService loanService;
    @Reference(interfaceClass = UserService.class, timeout = 20000, version = "1.0.0")
    UserService userService;
    @Reference(interfaceClass = BidInfoService.class, timeout = 20000, version = "1.0.0")
    BidInfoService bidInfoService;

    @RequestMapping("/index")
    public String index(Model model){

        //动力金融网历史年化收益率
        Double historyAverageRate = loanService.queryHistoryAverageRate();
        model.addAttribute("historyAverageRate", Math.round(historyAverageRate*100)/100.0);

        //平台用户数
        Long usersNum = userService.countUsers();
        model.addAttribute("usersNum", usersNum);

        //累计成交额
        Double sumBidMoney = bidInfoService.querySumBidMoney();
        model.addAttribute("sumBidMoney", sumBidMoney);


        return "index";
    }



}
