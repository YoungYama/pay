package com.yzz.alipay.ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.yzz.util.AlipayConfig;

/**
 * 功能：支付宝页面跳转同步通知页面（支付宝执行结果显示页，页面跳转）
 * 版本：3.2
 * 日期：2011-03-17
 * 说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 *
 * //***********页面功能说明***********
 * 该页面可在本机电脑测试
 * 可放入HTML等美化页面的代码、商户业务逻辑程序代码
 * TRADE_FINISHED(表示交易已经成功结束，并不能再对该交易做后续操作);
 * TRADE_SUCCESS(表示交易已经成功结束，可以对该交易做后续操作，如：分润、退款等);
 * ****************************************
 * 1、同步返回接口，作为参数传递给支付宝 
 * 2、用户付款成功后，从支付宝跳转到这个页面 
 * 3、在这个页面中加入相关业务处理，比如更新记录，标记付款成功信息。 
 * 4、需要对支付宝传递过来的签名进行认证。 
 * 5、用来展现成功付款信息给前台付款用户。 
 * 6、支付宝那边只返回一次。 
 * @author yzz
 *
 */
@WebServlet("/ReturnUrlServlet")
public class ReturnUrlServlet extends HttpServlet {

	private static final long serialVersionUID = 8005236181530057316L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AlipayConfig alipayConfig = AlipayConfig.getInstance();
		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}

		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 商户订单号

		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

		// 支付宝交易号

		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
		System.out.println("----------------return--------------");
		System.out.println("商户订单号" + out_trade_no + "\n支付宝交易号" + trade_no);
		System.out.println("----------------return--------------");
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		// 计算得出通知验证结果
		// boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String
		// publicKey, String charset, String sign_type)
		boolean verify_result;
		PrintWriter out = null;
		try {
			verify_result = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPulicKey(),
					alipayConfig.getCharset(), alipayConfig.getSignType());

			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			if (verify_result) {// 验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				// 请在这里加上商户的业务逻辑程序代码
				// 该页面可做页面美工编辑

				out.println("验证成功");
				// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

				//////////////////////////////////////////////////////////////////////////////////////////
			} else {
				// 该页面可做页面美工编辑

				out.println("验证失败");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}

	}

}
