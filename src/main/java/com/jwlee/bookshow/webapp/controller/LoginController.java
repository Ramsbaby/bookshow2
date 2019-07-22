package com.jwlee.bookshow.webapp.controller;

import com.jwlee.bookshow.common.util.EncryptUtil;
import com.jwlee.bookshow.webapp.common.*;
import com.jwlee.bookshow.webapp.db.login.model.Login;
import com.jwlee.bookshow.webapp.db.login.repository.LoginRepository;
import com.jwlee.bookshow.webapp.db.login.service.LoginService;
import com.jwlee.bookshow.webapp.db.menu.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/login")
@Controller
public class LoginController extends MsgService {
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource(name = "loginService")
    private LoginService loginService;

    @Resource(name = "menuService")
    private MenuService menuService;

    @Autowired
    private LoginRepository loginRepository;

    // 계정 추가
    @RequestMapping(value="/userConf/addAccount.do")
    @ResponseBody
    public ReturnData addAccount(@RequestParam Map<String, Object> reqMap, HttpSession session) {
        try {
            ReturnData returnData = new ReturnData();

            String userId = reqMap.get("userId").toString();
            String userName = reqMap.get("userName").toString();
            String password = reqMap.get("password").toString();
            String cellTel= reqMap.get("cellTel").toString();

            Login login = new Login();
            login = loginRepository.findByUserId(userId);

            if(login != null)
            {
                returnData.setResultData(getUserIdDuplicateMessage());
                return returnData;
            }
            else
            {
                EncryptUtil encryptUtil = new EncryptUtil();
                password = encryptUtil.encryptSHA256(password, userId.getBytes());

                login = new Login(userId,password,userName,cellTel);
                return loginService.addAccount(login);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnData(new ErrorInfo(e));
        }
    }

    /**
     * 계정 로그인
     **/
    @RequestMapping(value="prcsLogin.do", method=RequestMethod.POST)
    public @ResponseBody ReturnData prcsLogin(@RequestBody Map<String, Object> reqMap, HttpServletRequest request) throws Exception{
        ReturnData returnData = new ReturnData();
        try {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            reqMap.put("loginDateTime", dateFormat.format(date));

            String userId = reqMap.get("userId").toString();
            String password = reqMap.get("password").toString();

            // 패스워드 암호화 - SHA256
            EncryptUtil encryptUtil = new EncryptUtil();
            password = encryptUtil.encryptSHA256(password, userId.getBytes());

            // DB 확인
            Login login = new Login();
            login = loginRepository.findByUserId(userId);

            if(login == null)
            {
                throw new CustomException("아이디가 존재하지 않습니다.");
            }
            else if(!login.matchPassword(password))
            {//비밀번호 불일치
                throw new CustomException("비밀번호가 일치하지 않습니다.");
            }

            // 세션에 정보 저장
//            criterion.addParam("auth", userDto.getAuth());
//            criterion.addParam("codeId", "EVT_LEVEL");

            //MENU 셋팅
            SessionManager.setUser(userId, menuService.getSiteMenuList(reqMap).getResultData());

            returnData.setResultData(("로그인 성공 !"));
        } catch(Exception e) {
            e.printStackTrace();
            returnData = new ReturnData(new ErrorInfo(e));
        }
        return returnData;
    }

    /**
     * 로그아웃
     * @param session
     * @return
     */
    @RequestMapping(value="prcsLogout.do", method=RequestMethod.POST)
    public String prcsLogout(HttpSession session) {
        ReturnData returnData = new ReturnData();
        try {
            session.invalidate();
        } catch(Exception e) {
            e.printStackTrace();
            returnData.setHasError(true);
        }
        return "redirect:/login.do";
    }



}

