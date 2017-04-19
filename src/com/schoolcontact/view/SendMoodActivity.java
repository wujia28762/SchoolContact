package com.schoolcontact.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.Base.ScContext;
import com.schoolcontact.model.AttachmentInfo;
import com.schoolcontact.model.GroupInfo;
import com.schoolcontact.model.ItemAttachment;
import com.schoolcontact.model.ReturnMessage;
import com.schoolcontact.service.GroupService;
import com.schoolcontact.service.UserService;
import com.schoolcontact.utils.DialogUtils;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.ImageUtil;
import com.schoolcontact.utils.LoadingDialog;
import com.schoolcontact.utils.PicSelectUtils;
import com.schoolcontact.utils.StringUtils;

public class SendMoodActivity extends BaseActivity {

	private GridView gridView1; // ������ʾ����ͼ
	// private Button buttonPublish; // ������ť
	private final int IMAGE_OPEN = 1; // ��ͼƬ���
	private String pathImage; // ѡ��ͼƬ·��
	private UserService cs = new UserService();
	private GroupService gs = new GroupService();
	// private Bitmap bmp; // ������ʱͼƬ
	private boolean IsSubmit;
	private Handler mHandler;
	private boolean HasImp;
	private List<HashMap<String, Object>> imageItem;
	private SimpleAdapter simpleAdapter; // ������
	private EditText et_content;
	private Dialog dialog;
	private SendMoodActivity instance = this;
	private List<String> data = new ArrayList<String>();
	private TextView loginTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * ��ֹ���̵�ס����� ��ϣ���ڵ�����activity���� android:windowSoftInputMode="adjustPan"
		 * ϣ����̬�����߶� android:windowSoftInputMode="adjustResize"
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		setContentView(R.layout.activity_sendmood);
		// ��ȡ�ؼ�����
		onInit();
	}

	// ��ȡͼƬ·�� ��ӦstartActivityForResult
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// ��ͼƬ
		if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
			Uri uri = data.getData();
			if (!TextUtils.isEmpty(uri.getAuthority())) {
				// ��ѯѡ��ͼƬ
				Cursor cursor = getContentResolver().query(uri,
						new String[] { MediaStore.Images.Media.DATA }, null,
						null, null);
				// ���� û�ҵ�ѡ��ͼƬ
				if (null == cursor) {
					return;
				}
				// ����ƶ�����ͷ ��ȡͼƬ·��
				cursor.moveToFirst();
				pathImage = cursor.getString(cursor
						.getColumnIndex(MediaStore.Images.Media.DATA));
				System.out.println("!!!!!!!!!!!" + pathImage);
			}
		} // end if ��ͼƬ
	}

	// ˢ��ͼƬ
	@Override
	protected void onResume() {
		super.onResume();
		if (!TextUtils.isEmpty(pathImage)) {
			Bitmap addbmp = null;
			ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();
			SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(addbmp,
					queue);
			if (bitmapref != null) {
				addbmp = (Bitmap) bitmapref.get();
			}
			addbmp = ImageUtil.getSmallBitmap(pathImage);

			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", addbmp);
			map.put("itemText", "");
			map.put("FinshText", "");
			imageItem.add(map);

			simpleAdapter = new SimpleAdapter(this, imageItem,
					R.layout.griditem_addpic, new String[] { "itemImage",
							"itemText", "FinshText" },
					new int[] { R.id.imageView1, R.id.image_text,
							R.id.image_finish });
			simpleAdapter.setViewBinder(new ViewBinder() {
				@Override
				public boolean setViewValue(View view, Object data,
						String textRepresentation) {
					if (view instanceof ImageView && data instanceof Bitmap) {
						ImageView i = (ImageView) view;
						i.setImageBitmap((Bitmap) data);

						return true;
					}

					return false;
				}
			});
			gridView1.setAdapter(simpleAdapter);
			simpleAdapter.notifyDataSetChanged();
			// ˢ�º��ͷŷ�ֹ�ֻ����ߺ��Զ�����
			try {
				cs.uploadMoodImg(
						ImageUtil.compressImage(
								PicSelectUtils.getRealFilePath(instance,
										Uri.parse(pathImage)), "TEMP.jpg", 60),
						map, instance);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			pathImage = null;
		}
	}

	/*
	 * Dialog�Ի�����ʾ�û�ɾ������ positionΪɾ��ͼƬλ��
	 */
	protected void dialog(final int position) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("ȷ���Ƴ�������ͼƬ��");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				imageItem.remove(position);
				simpleAdapter.notifyDataSetChanged();
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.login_title:
//			if(sccontext.getUi().getUser_group().equals("��ʦ")){
//				dialog.show();
//			}else {
//				Toast.makeText(this, "ѧ��ֻ���ڵ�ǰ�༶��������", Toast.LENGTH_SHORT).show();
//			}
//			break;
		case R.id.tv_submit:

			boolean can = canSubmit(imageItem);
			if ((can && imageItem.size() > 1)
					|| !TextUtils.isEmpty(et_content.getText().toString())) {

				if (TextUtils.isEmpty(sccontext.getCurr_group())) {
					dialog.show();
					dialog.setCanceledOnTouchOutside(true);
				} else {
					mDialog.show();
					gs.publishGroup(data, et_content.getText().toString(), this);
					// IsSubmit = false;
				}

			} else if (!can) {
				Toast.makeText(this, "ͼƬ�ϴ���...", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "д��ʲô�ɣ�", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.backtext:

			DialogUtils.getAlertDialog("ȷ��ȡ���༭ô��", this);

			break;

		default:
			break;
		}
	}
	
	@Override
	public void dealMessage(ReturnMessage rm) {

		switch (rm.getEvent()) {
//		case EventList.UPLOADIMAGEPROGRESS:
//			uploadImagePb.setVisibility(View.VISIBLE);
//			uploadImagePb.setProgress(Integer.valueOf(rm.getMess()));
//			if(Integer.valueOf(rm.getMess())==100){
//				Timer timer = new Timer();
//				TimerTask timeTask = null;
//				if (timeTask == null) {
//					timeTask = new TimerTask() {
//						public void run() {
//							uploadPbHandler.sendEmptyMessage(1);
//						}
//					};
//				}
//				timer.schedule(timeTask, 500);
//			}
//			break;
		case EventList.ADDATTACHMENT:
			simpleAdapter.notifyDataSetChanged();
//			if (rm.getCode().equals(EventList.SCUESS)) {
//				simpleAdapter.notifyDataSetChanged();
//				Toast.makeText(this, "���ظ����ɹ�", Toast.LENGTH_SHORT).show();
//				if (canSubmit(imageItem)) {
//					canSubmit = true;
//				}
				
				// if (rm.getData() instanceof AttachmentInfo) {
				//
				// if (IsSubmit && canSubmit(imageItem)) {
				// if (TextUtils.isEmpty(sccontext.getCurr_group())) {
				// dialog.show();
				// dialog.setCanceledOnTouchOutside(true);
				// gs.publishGroup(data, et_content.getText()
				// .toString(), this);
				// IsSubmit = false;
				// }
				// }
				// } else {
				// Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT)
				// .show();
				// }
//			}
//			break;
		case EventList.SENDMOOD:
			if (mDialog.isShowing()) {
				mDialog.dismiss();
			}
			if (rm.getCode().equals("y")) {

		//		Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();

				data.clear();
				this.finish();
			} else {
		//		Toast.makeText(this, rm.getMess(), Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void InitChooseGroupDialog() {

		dialog = new Dialog(this);
		// dialog.show();
		// dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle("������");
		Window window = dialog.getWindow();

		// window.setGravity(Gravity.BOTTOM);
		// window.setWindowAnimations(R.style.Animation_Dialog_Bottom);
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// window.setBackgroundDrawable(new ColorDrawable(0));
		dialog.setTitle(R.string.chooseGroup);
		// View view = window.getDecorView();

		LinearLayout listLayout = new LinearLayout(this);
		listLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		listLayout.setLayoutParams(llParams);

		if (sccontext.getTechCurrGroupInfos() != null)
			for (GroupInfo groupInfo : sccontext.getTechCurrGroupInfos()) {
				Button btn1 = new Button(this);
				btn1.setBackgroundColor(Color.WHITE);
				btn1.setText(groupInfo.getGroup_name());
				btn1.setTag(groupInfo.getGroup_id());
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						FrameLayout.LayoutParams.MATCH_PARENT,
						FrameLayout.LayoutParams.WRAP_CONTENT);
				params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
				params.bottomMargin = 10;
				listLayout.addView(btn1);
//				window.addContentView(btn1, params);
				btn1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						// Intent it = new Intent();
						String curr_groupName = ((Button) v).getText()
								.toString();
						String curr_group = ((Button) v).getTag().toString();
						ScContext.getInstance().setCurr_group(curr_group);
						ScContext.getInstance().setCurr_groupName(curr_groupName);
						
						loginTitle.setText("������"+sccontext.getCurr_groupName());

						if (ScContext.getInstance().getCurr_user() != null) {
							
							ScContext
							.getInstance()
							.getmPreferences()
							.edit()
							.putString(
									"userdisplaygroupid_"
											+ ScContext.getInstance()
													.getCurr_user(),
									StringUtils.enCorder((((Button) v)
											.getTag().toString())))
							.commit();
					
							ScContext
							.getInstance()
							.getmPreferences()
							.edit()
							.putString(
									"userdisplaygroupname_"
											+ ScContext.getInstance()
													.getCurr_user(),
									StringUtils.enCorder((((Button) v)
											.getText().toString())))
							.commit();
							
							ScContext
									.getInstance()
									.getmPreferences()
									.edit()
									.putString(
											"usercurrgroupid_"
													+ ScContext.getInstance()
															.getCurr_user(),
											StringUtils.enCorder(curr_group))
									.commit();
							ScContext
									.getInstance()
									.getmPreferences()
									.edit()
									.putString(
											"usercurrgroupname_"
													+ ScContext.getInstance()
															.getCurr_user(),
											StringUtils
													.enCorder(curr_groupName))
									.commit();
							/*mDialog.show();
							gs.publishGroup(data, et_content.getText()
									.toString(), instance);*/
						}

					}
				});
			}
		
		ScrollView scroller = new ScrollView(this);
		scroller.addView(listLayout);
		window.addContentView(scroller, llParams);

	}

	@Override
	public void onInit() {

		mDialog = new LoadingDialog(this);
		loginTitle = (TextView) findViewById(R.id.login_title);
		loginTitle.setOnClickListener(this);
		if(sccontext.getCurr_groupName()==null){
			loginTitle.setText("��������");
		}else {
			loginTitle.setText("������"+sccontext.getCurr_groupName());
		}
		findViewById(R.id.backtext).setOnClickListener(this);
		mHandler = new Handler();
		InitChooseGroupDialog();
		et_content = (EditText) findViewById(R.id.et_add_content);
		findViewById(R.id.tv_submit).setOnClickListener(this);
		gridView1 = (GridView) findViewById(R.id.gridView1);
		mDialog.setText(R.string.publishing);
		/*
		 * ����Ĭ��ͼƬ����ͼƬ�Ӻ� ͨ��������ʵ�� SimpleAdapter����imageItemΪ����Դ
		 * R.layout.griditem_addpicΪ����
		 */
		Bitmap bmp = null;
		ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();
		SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bmp, queue);
		if (bitmapref != null) {
			bmp = (Bitmap) bitmapref.get();
		}
		// ��ȡ��ԴͼƬ�Ӻ�
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.add_img);
		imageItem = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("itemImage", bmp);
		map.put("itemText", "index");
		map.put("FinshText", "");
		imageItem.add(map);

		simpleAdapter = new SimpleAdapter(this, imageItem,
				R.layout.griditem_addpic, new String[] { "itemImage",
						"itemText", "FinshText" }, new int[] { R.id.imageView1,
						R.id.image_text, R.id.image_finish });
		/*
		 * HashMap����bmpͼƬ��GridView�в���ʾ,�������������ԴID����ʾ �� map.put("itemImage",
		 * R.drawable.img); �������: 1.�Զ���̳�BaseAdapterʵ�� 2.ViewBinder()�ӿ�ʵ�� �ο�
		 * http://blog.csdn.net/admin_/article/details/7257901
		 */
		simpleAdapter.setViewBinder(new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				// TODO Auto-generated method stub
				if (view instanceof ImageView && data instanceof Bitmap) {
					ImageView i = (ImageView) view;
					i.setImageBitmap((Bitmap) data);
					return true;
				}
				return false;
			}
		});
		gridView1.setAdapter(simpleAdapter);

		/*
		 * ����GridView����¼� ����:�ú���������󷽷� ����Ҫ�ֶ�����import android.view.View;
		 */
		gridView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if (imageItem.size() == 10 && position == 0) { // ��һ��ΪĬ��ͼƬ
					Toast.makeText(SendMoodActivity.this, "ͼƬ��9������",
							Toast.LENGTH_SHORT).show();
				} else if (position == 0) { // ���ͼƬλ��Ϊ+ 0��Ӧ0��ͼƬ
					Toast.makeText(SendMoodActivity.this, "����ͼƬ",
							Toast.LENGTH_SHORT).show();
					// ѡ��ͼƬ
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, IMAGE_OPEN);
					// ͨ��onResume()ˢ������
				} else {
					dialog(position);
					// Toast.makeText(MainActivity.this, "�����"+(position +
					// 1)+" ��ͼƬ",
					// Toast.LENGTH_SHORT).show();
				}
			}
		});
	//	uploadImagePb = (ProgressBar) findViewById(R.id.uploadImagePb);
	//	uploadImagePb.setVisibility(View.GONE);
	}

	public boolean canSubmit(List<HashMap<String, Object>> imageItem) {

		boolean isFinsh = true;
		List<String> data1 = new ArrayList<String>();
		for (HashMap<String, Object> i : imageItem) {

			if (!i.get("itemText").toString().equals("index"))
				data1.add(i.get("itemText").toString());
			if (i.get("itemText").toString().equals("")) {
				isFinsh = false;
				
				break;
			}
		}
		if (isFinsh) {
			data.clear();
			data.addAll(data1);
		}
		return isFinsh;

	}

}