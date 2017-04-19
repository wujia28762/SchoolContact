package com.schoolcontact.message;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

import java.io.UnsupportedEncodingException;

import me.add1.common.ParcelUtils;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.util.Log;

@MessageTag(value = "NoticeMessage", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class NotificationMessage extends MessageContent {

	private String message_type;
	private String content;
    private String fromname;
    private String tonames;
    private String attachments;

    public NotificationMessage(String message_type, String content, String fromname, String tonames,
    		String attachments) {
		this.message_type = message_type;
		this.content = content;
		this.fromname = fromname;
		this.tonames = tonames;
		this.attachments = attachments;
	}

	@SuppressWarnings("unchecked")
	public NotificationMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            
            setMessage_type(jsonObj.getString("message_type"));
            setContent(jsonObj.getString("content"));
            setFromname(jsonObj.getString("fromname"));
            setTonames(jsonObj.getString("tonames"));
            setAttachments(jsonObj.getString("attachments"));
            if(jsonObj.has("user")){
                setUserInfo(parseJsonToUserInfo(jsonObj.getJSONObject("user")));
        }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
        	jsonObj.put("message_type", message_type);
            jsonObj.put("content", content);
            jsonObj.put("fromname", fromname);
            jsonObj.put("tonames", tonames);
            jsonObj.put("attachments", attachments);

            if(getJSONUserInfo() != null)
                jsonObj.putOpt("user",getJSONUserInfo());

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    
    /**
     * 构造函数。
     *
     * @param in 初始化传入的 Parcel。
     */
    public NotificationMessage(Parcel in) {
    	setMessage_type(ParcelUtils.readFromParcel(in));
        setContent(ParcelUtils.readFromParcel(in));
        setFromname(ParcelUtils.readFromParcel(in));
        setTonames(ParcelUtils.readFromParcel(in));
        setAttachments(ParcelUtils.readFromParcel(in));
        setUserInfo(ParcelUtils.readFromParcel(in,UserInfo.class));
    }

	/**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<NotificationMessage> CREATOR = new Creator<NotificationMessage>() {

        @Override
        public NotificationMessage createFromParcel(Parcel source) {
            return new NotificationMessage(source);
        }

        @Override
        public NotificationMessage[] newArray(int size) {
            return new NotificationMessage[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	ParcelUtils.writeToParcel(dest, message_type);
        ParcelUtils.writeToParcel(dest, content);
        ParcelUtils.writeToParcel(dest, fromname);
        ParcelUtils.writeToParcel(dest, tonames);
        ParcelUtils.writeToParcel(dest, attachments);
        ParcelUtils.writeToParcel(dest,getUserInfo());
    }

    public String getMessage_type() {
		return message_type;
	}
	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFromname() {
		return fromname;
	}
	public void setFromname(String fromname) {
		this.fromname = fromname;
	}
	public String getTonames() {
		return tonames;
	}
	public void setTonames(String tonames) {
		this.tonames = tonames;
	}
	public String getAttachments() {
		return attachments;
	}
	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}
    
}
