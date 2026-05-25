package com.search.platform.controller;

import com.search.platform.wxmp.WxMpConstant;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts.MenuButtonType;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信公众号相关接口。
 */
@RestController
@RequestMapping("/")
@Slf4j
public class WxMpController {

    @Resource
    private WxMpService wxMpService;

    @Resource
    private WxMpMessageRouter router;

    @PostMapping("/")
    public void receiveMessage(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            response.getWriter().println("非法请求");
        }
        String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ? "raw"
                : request.getParameter("encrypt_type");
        if ("raw".equals(encryptType)) {
            return;
        }
        if ("aes".equals(encryptType)) {
            String msgSignature = request.getParameter("msg_signature");
            WxMpXmlMessage inMessage = WxMpXmlMessage
                    .fromEncryptedXml(request.getInputStream(), wxMpService.getWxMpConfigStorage(), timestamp,
                            nonce, msgSignature);
            log.info("message content = {}", inMessage.getContent());
            WxMpXmlOutMessage outMessage = router.route(inMessage);
            if (outMessage == null) {
                response.getWriter().write("");
            } else {
                response.getWriter().write(outMessage.toEncryptedXml(wxMpService.getWxMpConfigStorage()));
            }
            return;
        }
        response.getWriter().println("不可识别的加密类型");
    }

    @GetMapping("/")
    public String check(String timestamp, String nonce, String signature, String echostr) {
        log.info("check");
        if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "";
    }

    /**
     * 设置公众号菜单。
     */
    @GetMapping("/setMenu")
    public String setMenu() throws WxErrorException {
        log.info("setMenu");
        WxMenu wxMenu = new WxMenu();

        WxMenuButton menu1 = new WxMenuButton();
        menu1.setType(MenuButtonType.VIEW);
        menu1.setName("主菜单一");
        WxMenuButton menu1Sub = new WxMenuButton();
        menu1Sub.setType(MenuButtonType.VIEW);
        menu1Sub.setName("项目主页");
        menu1Sub.setUrl("http://localhost:8102/api/doc.html");
        menu1.setSubButtons(Collections.singletonList(menu1Sub));

        WxMenuButton menu2 = new WxMenuButton();
        menu2.setType(MenuButtonType.CLICK);
        menu2.setName("点击事件");
        menu2.setKey(WxMpConstant.CLICK_MENU_KEY);

        WxMenuButton menu3 = new WxMenuButton();
        menu3.setType(MenuButtonType.VIEW);
        menu3.setName("主菜单三");
        WxMenuButton menu3Sub = new WxMenuButton();
        menu3Sub.setType(MenuButtonType.VIEW);
        menu3Sub.setName("接口文档");
        menu3Sub.setUrl("http://localhost:8102/api/doc.html");
        menu3.setSubButtons(Collections.singletonList(menu3Sub));

        wxMenu.setButtons(Arrays.asList(menu1, menu2, menu3));
        wxMpService.getMenuService().menuCreate(wxMenu);
        return "ok";
    }
}
