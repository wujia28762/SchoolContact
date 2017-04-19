package com.schoolcontact.service;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.schoolcontact.Base.MessageCallback;
import com.schoolcontact.model.FeedBackInfo;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.utils.EventList;

/**
 * @author Star
 * @description ͨ�÷�����
 */
public class CommonService extends BaseService {

	private static String TAG = "CommonService";
	private String url = baseUrl + EventList.feedbackAjax;

	public  int getVerCode(Context context) {  
        int verCode = -1;  
        try {  
            verCode = context.getPackageManager().getPackageInfo(  
                    "com.myapp", 0).versionCode;  
        } catch (NameNotFoundException e) {  
            Log.e(TAG  , e.getMessage());
        }  
        return verCode;  
    }
	  public  String getVerName(Context context) {  
	        String verName = "";  
	        try {  
	            verName = context.getPackageManager().getPackageInfo(  
	                    "com.myapp", 0).versionName;  
	        } catch (NameNotFoundException e) {  
	            Log.e(TAG, e.getMessage());  
	        }  
	        return verName;     
	}  
	/**
	 * @param content
	 *            ��������
	 * @param mc
	 *            �ص���������
	 * @description ���ͷ����������
	 */
	public void sendFeedback(final String content, final MessageCallback mc) {
		
		if (sccontext.getUi() != null) {
			AjaxParams params = new AjaxParams();
			params.put("actor", EventList.ACTOR_FEEDBACK);
			params.put("data", EventList.DATA_FEEDBACK);
			params.put("content", content);
			params.put("user_name", sccontext.getUi().getUsername());
			params.put("user_id", sccontext.getUi().getUser_id());
			params.put("domain", sccontext.getUi().getDomain());
			// TODO �ֻ�����δ�޸�
			params.put("user_mobile", "122111");
			System.out.println(FinalHttp.getUrlWithQueryString(url, params));
			fh.get(url, params, new AjaxCallBack<String>() {

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {

					mc.dealMessage(new ReturnMessage("error", "����ʱ��",
							EventList.FEEDBACK, null));
				}

				@Override
				public void onSuccess(String t) {
					try {
						Log.v("��������", t);
						FeedBackInfo li = objectMapper.readValue(t,
								FeedBackInfo.class);
						if (li.isScuess(li)) {

							mc.dealMessage(new ReturnMessage(li.getCode(), li
									.getMess(), EventList.FEEDBACK, null));
						}
						mc.dealMessage(new ReturnMessage(li.getCode(), li
								.getMess(), EventList.FEEDBACK, null));

					} catch (Exception e) {
						System.out.println("���������쳣");
						e.printStackTrace();
					}
				}

			});
		}
	}
public void GetNewVersion(final String content, final MessageCallback mc) {
		
		if (sccontext.getUi() != null) {
			AjaxParams params = new AjaxParams();
			params.put("actor", EventList.ACTOR_FEEDBACK);
			params.put("data", EventList.DATA_FEEDBACK);
			params.put("content", content);
			params.put("user_name", sccontext.getUi().getUsername());
			params.put("user_id", sccontext.getUi().getUser_id());
			params.put("domain", sccontext.getUi().getDomain());
			// TODO �ֻ�����δ�޸�
			params.put("user_mobile", "122111");
			System.out.println(FinalHttp.getUrlWithQueryString(url, params));
			fh.get(url, params, new AjaxCallBack<String>() {

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {

					mc.dealMessage(new ReturnMessage("error", "����ʱ��",
							EventList.FEEDBACK, null));
				}

				@Override
				public void onSuccess(String t) {
					try {
						Log.v("��������", t);
						FeedBackInfo li = objectMapper.readValue(t,
								FeedBackInfo.class);
						if (li.isScuess(li)) {

							mc.dealMessage(new ReturnMessage(li.getCode(), li
									.getMess(), EventList.FEEDBACK, null));
						}
						mc.dealMessage(new ReturnMessage(li.getCode(), li
								.getMess(), EventList.FEEDBACK, null));

					} catch (Exception e) {
						System.out.println("���������쳣");
						e.printStackTrace();
					}
				}

			});
		}
	}
public void downNewPackge(final MessageCallback mc) {
	
	if (sccontext.getUi() != null) {
		AjaxParams params = new AjaxParams();
		params.put("actor", EventList.ACTOR_FEEDBACK);
		params.put("data", EventList.DATA_FEEDBACK);
		params.put("content", "1212");
		params.put("user_name", sccontext.getUi().getUsername());
		params.put("user_id", sccontext.getUi().getUser_id());
		params.put("domain", sccontext.getUi().getDomain());
		// TODO �ֻ�����δ�޸�
		params.put("user_mobile", "122111");
		System.out.println(FinalHttp.getUrlWithQueryString(url, params));
		fh.get(url, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {

				mc.dealMessage(new ReturnMessage("error", "����ʱ��",
						EventList.FEEDBACK, null));
			}

			@Override
			public void onSuccess(String t) {
				try {
					Log.v("��������", t);
					FeedBackInfo li = objectMapper.readValue(t,
							FeedBackInfo.class);
					if (li.isScuess(li)) {

						mc.dealMessage(new ReturnMessage(li.getCode(), li
								.getMess(), EventList.FEEDBACK, null));
					}
					mc.dealMessage(new ReturnMessage(li.getCode(), li
							.getMess(), EventList.FEEDBACK, null));

				} catch (Exception e) {
					System.out.println("���������쳣");
					e.printStackTrace();
				}
			}

		});
	}
}

}
