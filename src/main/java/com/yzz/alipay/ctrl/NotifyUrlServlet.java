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
 * 1、异步通知接口，作为参数传递给支付宝。 （后台通知）
 * 2、如果不传递，则不通知。 
 * 3、相关业务逻辑应该和return_url中相同。 
 * 4、返回字符串"success"或者"fail"，不能带有任何HTML信息。 
 * 5、付款成功后就通知一次，如果不成功，1分钟、3分钟、10分钟、半个小时。。。后再通知，直到返回success。 
 * 6、过期时间是48小时，如果48小时内都通知不成功，那么就不再通知。
 *  
 * 掉单：支付宝付款成功，但是没有收到成功的返回信息。 
 * 如果不传递notify_url，掉单率在20-30%，准备和alipay对账吧。 
 * notify_url可以保证99.99%的通知成功率。 
 * 
 * @author yzz
 *
 */
@WebServlet("/NotifyUrlServlet")
public class NotifyUrlServlet extends HttpServlet {

	private static final long serialVersionUID = 8005236181530057316L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AlipayConfig alipayConfig = AlipayConfig.getInstance();
		// 获取支付宝POST过来反馈信息
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
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 商户订单号

		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
		// 支付宝交易号

		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

		// 交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
		System.out.println("----------------notify--------------");
		System.out.println("交易状态" + trade_status + "\n商户订单号" + out_trade_no + "\n支付宝交易号" + trade_no);
		System.out.println("----------------notify--------------");
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

				// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

				if (trade_status.equals("TRADE_FINISHED")) {
					// 判断该笔订单是否在商户网站中已经做过处理
					// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
					// 如果有做过处理，不执行商户的业务程序

					// 注意：
					// 如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
					// 如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
				} else if (trade_status.equals("TRADE_SUCCESS")) {
					// 判断该笔订单是否在商户网站中已经做过处理
					// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
					// 如果有做过处理，不执行商户的业务程序

					// 注意：
					// 如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
				}

				// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
				out.println("success"); // 请不要修改或删除

				//////////////////////////////////////////////////////////////////////////////////////////
			} else {// 验证失败
				out.println("fail");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}

	}

}
