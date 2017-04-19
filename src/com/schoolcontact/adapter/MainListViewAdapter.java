package com.schoolcontact.adapter;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.school_contact.main.R;
import com.schoolcontact.Base.MessageCallback;
import com.schoolcontact.Base.ScContext;
import com.schoolcontact.model.CommentInfo;
import com.schoolcontact.model.FriendMessageInfo;
import com.schoolcontact.model.GroupInfo;
import com.schoolcontact.service.GroupService;
import com.schoolcontact.utils.AsyncImageLoader;
import com.schoolcontact.utils.DateUtil;
import com.schoolcontact.utils.DialogUtils;
import com.schoolcontact.utils.EventList;
import com.schoolcontact.utils.ListUtils;
import com.schoolcontact.view.PhotoAlbumActivity;
import com.schoolcontact.view.PyqActivity;
import com.schoolcontact.widget.CommentListView;
import com.schoolcontact.widget.GridView;

public class MainListViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private boolean isShow;
	private AsyncImageLoader imageLoader;
	private final Object _LOCK = new Object();

	private List<FriendMessageInfo> data;

	private boolean isAllowImage;
	public static boolean isBusy= false;
	private List<String> likePerson;
	private List<String> urlList;
	private List<CommentInfo> comments;
	private GroupService gs = new GroupService();
	private MainListViewAdapter ma = this;

	
	public void setData(List<FriendMessageInfo> data) {
		this.data = data;
	}

	public List<FriendMessageInfo> getData() {
		return data;
	}

	public MainListViewAdapter(Context context,AsyncImageLoader imageLoader) {
		this.context = context;
		inflater = LayoutInflater.from(context);

		this.imageLoader = imageLoader; 
	}

	public void loadDrawable(boolean flag) {

		synchronized (_LOCK) {

			isBusy = flag;
		}
	}

	
	@Override
	public int getCount() {
		if (data != null)
			return data.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int arg0) {

		if (data != null)
			return data.get(arg0);
		else
			return null;
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	public class ViewHolder {

		private TextView mTv_name;
		private ImageView mIv_post_head;
		private GridView mGridView;
		private GridView mImgGridView;
		
		private CommentListView mListView;
		private Button bt_action;
		private LinearLayout linearlayout;
		private HeadCommentGridViewAdapter mHeadCommentGridViewAdapter;
		private ImageView iv_head_img;
		private GridViewAdapter mGridViewAdapter;
		public GridViewAdapter getmGridViewAdapter() {
			return mGridViewAdapter;
		}
		private LinearLayout ll_zan;
		private LinearLayout ll_enterpinglun;
		private CommentListViewAdapter mCommentListViewAdapter;
		private Button inputbutton;
		private Dialog inputDialog;
		private EditText inputEditText;
		private TextView mTv_messagecontent;
		private TextView mTv_time;
		private LinearLayout mll_commentlist;
		private TextView mTv_delete;
		private TextView tv_zan;
		private View m_Line;
		private LinearLayout ll_back;
	}

	@Override
	public View getView(final int arg0, View convertView, ViewGroup arg2) {

		String groupId = data.get(arg0).getGroup_id();
		if(ScContext.getInstance().getDisplayGroupId()!=null&&groupId!=null){
			if(!groupId.equals(ScContext.getInstance().getDisplayGroupId())){
				ScContext.getInstance().setDisplayGroupId(groupId);
				ArrayList<GroupInfo> list = (ArrayList<GroupInfo>) ScContext.getInstance().getmGroupInfos();
				if(list==null||list.size()==0){
					
				}else{
					for(GroupInfo gi:list){
						if(gi.getGroup_id().equals(groupId)){
							ScContext.getInstance().setDisplayGroupName(gi.getGroup_name());
							Intent intent = new Intent();
							intent.setAction("com.school_contact.main.refreshgroupname");
							context.sendBroadcast(intent);
							break;
						}
					}
				}
				
			}
		}
		
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_main_item, null);
			// convertView.setLayoutParams(new
			// AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,
			// LayoutParams.WRAP_CONTENT));
			viewHolder = new ViewHolder();

			viewHolder.mll_commentlist = (LinearLayout) convertView
					.findViewById(R.id.ll_commentlist);
			viewHolder.iv_head_img = (ImageView) convertView
					.findViewById(R.id.iv_head_img);
			viewHolder.mTv_delete = (TextView) convertView
					.findViewById(R.id.tv_delete);
			viewHolder.mTv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.mTv_messagecontent = (TextView) convertView
					.findViewById(R.id.tv_messagecontent);
			viewHolder.mIv_post_head = (ImageView) convertView
					.findViewById(R.id.iv_post_head);
			viewHolder.mTv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.mGridView = (GridView) convertView
					.findViewById(R.id.gv_comment_head);
			viewHolder.mImgGridView = (GridView) convertView
					.findViewById(R.id.gv_listView_main_gridView);
			viewHolder.mListView = (CommentListView) convertView
					.findViewById(R.id.lv_item_listView);
			viewHolder.bt_action = (Button) convertView
					.findViewById(R.id.bt_action);
			viewHolder.linearlayout = (LinearLayout) convertView
					.findViewById(R.id.ll_pinglun);

			viewHolder.ll_back = (LinearLayout) convertView
					.findViewById(R.id.mainlist_background);
			viewHolder.ll_zan = (LinearLayout) convertView
					.findViewById(R.id.ll_zan);
			viewHolder.tv_zan = (TextView) convertView
					.findViewById(R.id.tv_zan);
			DialogUtils.getcustomDialog(context, R.layout.dialog_iputcomments);
			viewHolder.inputDialog = DialogUtils.getDialog();
			viewHolder.inputbutton = DialogUtils.getmButton();
			viewHolder.inputEditText = DialogUtils.getmEdittext();
			viewHolder.ll_enterpinglun = (LinearLayout) convertView
					.findViewById(R.id.ll_enterpinglun);
			// viewHolder.inputbutton

			viewHolder.m_Line = (View) convertView.findViewById(R.id.line_view);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 测试数据--------------------------
		if (!ListUtils.isEmpty(data)) {
			isShow = false;
			// List<String> n1 = new ArrayList<String>();
			// comments = new ArrayList<String>();
			urlList = new ArrayList<String>();
			viewHolder.linearlayout.setVisibility(View.INVISIBLE);
			if (data.get(arg0).getUid()
					.equals(ScContext.getInstance().getUi().getUsertradeid())) {

				viewHolder.ll_back.setBackgroundColor(context.getResources()
						.getColor(R.color.coral));
				viewHolder.mListView.setBackgroundColor(context.getResources()
						.getColor(R.color.touming50));
			} else {
				viewHolder.ll_back.setBackgroundColor(context.getResources()
						.getColor(R.color.white));
				viewHolder.mListView.setBackgroundColor(context.getResources()
						.getColor(R.color.hui));
			}
			likePerson = data.get(arg0).getThumbs();
			// System.out.println("!!!!!!!!!!!!!!!!!!!"+);
			if (!TextUtils.isEmpty(data.get(arg0).getFile_urls()))
				for (String url : data.get(arg0).getFile_urls().split("\\,")) {
					String tempUrl = EventList.BASEURL;
					if (url.startsWith("/"))
						url = url.replaceFirst("/", "");
					tempUrl = tempUrl.concat(url);
					Log.i("临时拼接URL", tempUrl);
					urlList.add(tempUrl);
				}

			comments = data.get(arg0).getComments();

			// 测试数据---------------------------
			if (data.get(arg0).getThumbs() != null
					&& data.get(arg0).getThumbs().size() > 0) {
				viewHolder.mll_commentlist.setVisibility(View.VISIBLE);
			} else {
				viewHolder.mll_commentlist.setVisibility(View.GONE);
			}
			viewHolder.mHeadCommentGridViewAdapter = new HeadCommentGridViewAdapter(
					context, likePerson);
			// if (viewHolder.mHeadCommentGridViewAdapter.getPersons() != null
			// && viewHolder.mHeadCommentGridViewAdapter.getPersons()
			// .size() != 0) {
			// viewHolder.mGridView.setVisibility(View.VISIBLE);
			// viewHolder.iv_head_img.setVisibility(View.VISIBLE);
			// }

			if (data.get(arg0).getUid()
					.equals(ScContext.getInstance().getUi().getUsertradeid())) {
				viewHolder.mTv_delete.setVisibility(View.VISIBLE);
				viewHolder.mTv_delete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new Builder(context);
						builder.setMessage("确定要删除此信息吗？");
						builder.setTitle("提示");

						// AlertDialog.Builder builder =
						// DialogUtils.getAlert("确定要删除此评论吗？",
						// (Activity)context);
						builder.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										gs.DeleteMessage(ma, data.get(arg0),
												(MessageCallback) context);

									}
								});
						builder.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						builder.create().show();

					}
				});
			} else {
				viewHolder.mTv_delete.setVisibility(View.GONE);

			}

			viewHolder.mGridViewAdapter = new GridViewAdapter(context, urlList,
					imageLoader);
			viewHolder.mCommentListViewAdapter = new CommentListViewAdapter(
					context, comments);

			viewHolder.mImgGridView.setVisibility(View.VISIBLE);
			viewHolder.mGridView
					.setAdapter(viewHolder.mHeadCommentGridViewAdapter);
			viewHolder.mImgGridView.setAdapter(viewHolder.mGridViewAdapter);
			viewHolder.mListView.setAdapter(viewHolder.mCommentListViewAdapter);

			if (data.get(arg0).getThumbs()
					.contains(ScContext.getInstance().getUi().getUsername()))
				viewHolder.tv_zan.setText("取消赞");
			else
				viewHolder.tv_zan.setText("赞    ");
//			viewHolder.mListView
//					.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//						@Override
//						public boolean onItemLongClick(AdapterView<?> parent,
//								View view, int position, long id) {
//
//							if (comments != null
//									&& ScContext.getInstance().getUi() != null) {
//
//								final CommentInfo commentinfo = (CommentInfo) (viewHolder.mCommentListViewAdapter
//										.getItem(position));
//								if (commentinfo.getUid().equals(
//										ScContext.getInstance().getUi()
//												.getUsertradeid())) {
//
//									
//
//								}
//
//							}
//							return false;
//						}
//					});
			viewHolder.mListView
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							if (comments != null) {
								final CommentInfo commentinfo = (CommentInfo) (viewHolder.mCommentListViewAdapter
										.getItem(position));
								if (ScContext.getInstance().getUi() != null
										&& !commentinfo.getUid().equals(
												ScContext.getInstance().getUi()
														.getUsertradeid())) {
									viewHolder.inputDialog.show();
									viewHolder.inputDialog
											.setCanceledOnTouchOutside(true);
									viewHolder.inputDialog.getWindow()
											.setLayout(
													LayoutParams.MATCH_PARENT,
													LayoutParams.WRAP_CONTENT);

									viewHolder.inputEditText.setHint("回复"
											+ commentinfo.getUname());
									// viewHolder.linearlayout.setVisibility(View.INVISIBLE);
									viewHolder.inputbutton
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													if (!TextUtils
															.isEmpty(viewHolder.inputEditText
																	.getText()
																	.toString())) {
														viewHolder.inputDialog
																.dismiss();

														CommentInfo ci = new CommentInfo();
														ci.setContent(viewHolder.inputEditText
																.getText()
																.toString());
														ci.setTo_uid(commentinfo
																.getUid());
														ci.setUid(ScContext
																.getInstance()
																.getUi()
																.getUsertradeid());
														ci.setTo_uname(commentinfo
																.getUname());
														ci.setUname(ScContext
																.getInstance()
																.getUi()
																.getUsername());
														gs.commentMessage(
																ci,
																data.get(arg0),
																viewHolder.mCommentListViewAdapter,
																(MessageCallback) context);

													} else {
														Toast.makeText(
																context,
																"输入些内容吧！",
																Toast.LENGTH_SHORT)
																.show();
													}
												}
											});

								} else {
									AlertDialog.Builder builder = new Builder(
											context);
									builder.setMessage("确定要删除此评论吗？");
									builder.setTitle("提示");

									// AlertDialog.Builder builder =
									// DialogUtils.getAlert("确定要删除此评论吗？",
									// (Activity)context);
									builder.setPositiveButton(
											"确认",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													gs.DeleteComment(
															data.get(arg0)
																	.getKey_id(),
															data.get(arg0)
																	.getUid(),
															viewHolder.mCommentListViewAdapter,
															commentinfo,
															(MessageCallback) context);

												}
											});
									builder.setNegativeButton(
											"取消",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}
											});
									builder.create().show();
								}
							}
						}
					});

			viewHolder.ll_zan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					viewHolder.mGridView.setVisibility(View.VISIBLE);
					viewHolder.iv_head_img.setVisibility(View.VISIBLE);

					viewHolder.linearlayout.setVisibility(View.INVISIBLE);
					if (ScContext.getInstance().getUi() != null
							) {

						// ScContext
						// .getInstance()
						// .getMgroupFriendMessageInfo()
						// .get(Curr_arg)
						// .getThumbs()
						// .add(ScContext.getInstance().getUi()
						// .getUsername());
						// viewHolder.mHeadCommentGridViewAdapter
						// .notifyDataSetChanged();
						gs.AgreeMessage(viewHolder.mHeadCommentGridViewAdapter,
								data.get(arg0), arg0, (MessageCallback) context);

					}

				}
			});
			viewHolder.ll_enterpinglun
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (ScContext.getInstance().getUi() != null) {
								viewHolder.inputDialog.show();
								viewHolder.inputDialog
										.setCanceledOnTouchOutside(true);
								viewHolder.inputDialog.getWindow().setLayout(
										LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT);

								viewHolder.linearlayout
										.setVisibility(View.INVISIBLE);
								viewHolder.inputbutton
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												if (!TextUtils
														.isEmpty(viewHolder.inputEditText
																.getText()
																.toString())) {
													viewHolder.inputDialog
															.dismiss();

													CommentInfo ci = new CommentInfo();
													ci.setContent(viewHolder.inputEditText
															.getText()
															.toString());
													ci.setTo_uid("0");
													ci.setUid(ScContext
															.getInstance()
															.getUi()
															.getUsertradeid());
													ci.setTo_uname("");
													ci.setUname(ScContext
															.getInstance()
															.getUi()
															.getUsername());
													gs.commentMessage(
															ci,
															data.get(arg0),
															viewHolder.mCommentListViewAdapter,
															(MessageCallback) context);

												} else {
													Toast.makeText(context,
															"输入些内容吧！",
															Toast.LENGTH_SHORT)
															.show();
												}

											}
										});

							}

						}
					});

			viewHolder.bt_action.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!isShow) {
						viewHolder.linearlayout.setVisibility(View.VISIBLE);

						isShow = true;
					} else {
						viewHolder.linearlayout.setVisibility(View.INVISIBLE);
						isShow = false;
					}
				}
			});

			viewHolder.mIv_post_head.setTag(data.get(arg0).getPortraitUri());
			if (!TextUtils.isEmpty(data.get(arg0).getPortraitUri())) {
				// System.out.println(urlList.get(arg0));
				// viewHolder.mImageView.setTag(viewHolder.mUrl);
				Bitmap bitmap = null;
				ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();  
			    SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap, queue);
			    if(bitmapref!=null){  
		            bitmap = (Bitmap) bitmapref.get();  
		        }
				bitmap = imageLoader.loadImage(viewHolder.mIv_post_head,
						data.get(arg0).getPortraitUri());
				if (bitmap != null) {
					viewHolder.mIv_post_head
							.setImageBitmap(toRoundBitmap(bitmap));
				}
			}
			viewHolder.mIv_post_head.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,
							PhotoAlbumActivity.class);
					intent.putExtra("uid", data.get(arg0).getUid());
					intent.putExtra("name", data.get(arg0).getUname());
					intent.putExtra("url", data.get(arg0).getPortraitUri());
					context.startActivity(intent);

				}
			});
			viewHolder.mTv_time.setText(DateUtil.formatTimeString(Long
					.valueOf(data.get(arg0).getSend_time())));
			viewHolder.mTv_messagecontent.setText(data.get(arg0).getContent());
			viewHolder.mTv_name.setText(data.get(arg0).getUname());
			viewHolder.mTv_name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,
							PhotoAlbumActivity.class);
					intent.putExtra("uid", data.get(arg0).getUid());
					intent.putExtra("name", data.get(arg0).getUname());
					intent.putExtra("url", data.get(arg0).getPortraitUri());
					context.startActivity(intent);
				}
			});
		}
		return convertView;
	}

	public Bitmap toRoundBitmap(Bitmap bitmap) {
		// 圆形图片宽高
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 正方形的边长
		int r = 0;
		// 取最短边做边长
		if (width > height) {
			r = height;
		} else {
			r = width;
		}
		// 构建一个bitmap
		Bitmap backgroundBmp = null;
		ReferenceQueue<Bitmap> queue = new ReferenceQueue<Bitmap>();  
	    SoftReference<Bitmap> bitmapref = new SoftReference<Bitmap>(bitmap, queue);
	    if(bitmapref!=null){  
	    	backgroundBmp = (Bitmap) bitmapref.get();  
        }
		backgroundBmp = Bitmap.createBitmap(width, height,
				Config.ARGB_4444);
		// new一个Canvas，在backgroundBmp上画图
		Canvas canvas = new Canvas(backgroundBmp);
		Paint paint = new Paint();
		// 设置边缘光滑，去掉锯齿
		paint.setAntiAlias(true);
		// 宽高相等，即正方形
		RectF rect = new RectF(0, 0, r, r);
		// 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
		// 且都等于r/2时，画出来的圆角矩形就是圆形
		canvas.drawRoundRect(rect, r / 2, r / 2, paint);
		// 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas将bitmap画在backgroundBmp上
		canvas.drawBitmap(bitmap, null, rect, paint);
		// 返回已经绘画好的backgroundBmp
		return backgroundBmp;
	}

	public boolean isAllowImage() {
		return isAllowImage;
	}

	public void setAllowImage(boolean isAllowImage) {
		this.isAllowImage = isAllowImage;
	}
}
