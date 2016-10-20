package com.server.web;

import com.server.dao.AddressDao;
import com.server.dao.WithdrawDao;
import com.server.model.Withdraw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class WelcomeController {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

    @Autowired
    AddressDao userDao;
    @Autowired
    WithdrawDao withdrawDao;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public List<Withdraw> welcome(Model model) {
        logger.debug("mkyong");
        List<Withdraw> users = withdrawDao.findAll();

        System.out.println(users);

        return users;

    }

}