package com.yzz.alipay.ctrl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.yzz.util.AlipayConfig;

/**
 * 功能：支付宝手机网站支付接口(alipay.trade.wap.pay)接口调试入口页面
 * 版本：2.0
 * 修改日期：2016-11-01
 * 说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 请确保项目文件有可写权限，不然打印不了日志。
 * @author yzz
 *
 */
@WebServlet(urlPatterns = "/AlipayTradeWapPay")
public class AlipayTradeWapPayServlet extends HttpServlet {

	private static final long serialVersionUID = 7245881131091242160L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("WIDout_trade_no") != null) {
			AlipayConfig alipayConfig = AlipayConfig.getInstance();

			// 商户订单号，商户网站订单系统中唯一订单号，必填
			String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			// 订单名称，必填
			String subject = new String(request.getParameter("WIDsubject").getBytes("ISO-8859-1"), "UTF-8");
			System.out.println(subject);
			// 付款金额，必填
			String total_amount = new String(request.getParameter("WIDtotal_amount").getBytes("ISO-8859-1"), "UTF-8");
			// 商品描述，可空
			String body = new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"), "UTF-8");
			// 超时时间 可空
			String timeout_express = "2m";
			// 销售产品码 必填
			String product_code = "QUICK_WAP_PAY";
			/**********************/
			// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
			// 调用RSA签名方式
			AlipayClient client = new DefaultAlipayClient(alipayConfig.getGatewayUrl(), alipayConfig.getAppid(),
					alipayConfig.getPrivateKey(), alipayConfig.getFormat(), alipayConfig.getCharset(),
					alipayConfig.getAlipayPulicKey(), alipayConfig.getSignType());
			AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

			// 封装请求支付信息
			AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
			model.setOutTradeNo(out_trade_no);
			model.setSubject(subject);
			model.setTotalAmount(total_amount);
			model.setBody(body);
			model.setTimeoutExpress(timeout_express);
			model.setProductCode(product_code);
			alipay_request.setBizModel(model);
			// 设置异步通知地址
			alipay_request.setNotifyUrl(alipayConfig.getNotifyUrl());
			// 设置同步地址
			alipay_request.setReturnUrl(alipayConfig.getReturnUrl());

			// form表单生产
			String form = null;
			PrintWriter out = null;
			try {
				// 调用SDK生成表单
				form = client.pageExecute(alipay_request).getBody();
				response.setContentType("text/html;charset=" + alipayConfig.getCharset());
				out = response.getWriter();
				out.write(form);// 直接将完整的表单html输出到页面
			} catch (AlipayApiException e) {
				e.printStackTrace();
			} finally {
				out.flush();
				out.close();
			}
		}
	}

}
