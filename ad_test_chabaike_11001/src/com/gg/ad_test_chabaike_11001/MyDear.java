package com.gg.ad_test_chabaike_11001;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class MyDear
{

	public static final String BASE_URL_ACTION = "http://218.31.79.219:16008/WL627/TestAction9990001";
	public static final String BASE_URL="http://218.31.79.219:16008";
	public static final String BASE_URL_PROJECT="http://218.31.79.219:16008/WL627";
	public static final String BASE_URL_TEST_JSONHTML="http://218.31.79.219:16008/WL627/tool/jsontest.html";
	
	private static final int REQUEST_TIMEOUT = 10*1000;          //������������ʱ10����   ͨ��������������������ӵĳ�ʱʱ��  
	private static final int WAITDATA_TIMEOUT = 20*1000;  //���õȴ����ݳ�ʱʱ��20����  Socket�����ݵĳ�ʱʱ�䣬��������ȡ��Ӧ���ݵȴ�ʱ��
	public  static boolean   server_response_is_normal = true;   //�жϷ��������缰��Ӧ�Ƿ�����
	public static int[] testImgSysResIds;
	public static List<HashMap<String, Object>> testListData;
	public static String[] testArrayStr =
	{ "hello", "my", "dear", "are", "you", "OK", "ding", "dang", "OK", "too" };
	
	public static Map<String,String> mapParameter = new HashMap<String,String>();
	
	interface CallBack
	{
		void callbackInvokeForResult(Object o,String... strs);
	}
	

	/**
	 * 
	 * ���캯����MyGeneralBaseAdapter(Context context,List<HashMap<String,Object>>
	 * listData,int itemLayoutID,int[] itemViewIDs)
	 * ������MyGeneralBaseAdapter��holder�д洢ItemViews,���帳ֵ��Ҫʵ��renderData����
	 * 
	 * @param context
	 *            -----������
	 * @param listData
	 *            -----����Դ���������null���Զ�����listData 50���������ݣ� �����������У�key��id
	 *            int��imgResId int��col1-col10 String��
	 * @param itemLayoutID
	 *            -----item�����ļ�
	 * @param itemViewIDs
	 *            -----item Views ID
	 * 
	 */
	public static abstract class MyGeneralBaseAdapter extends BaseAdapter
	{
		protected Context context;
		protected List<? extends Object> listData;
		protected int itemLayoutID;
		protected int[] itemViewIDs;

		/**
		 * ������MyGeneralBaseAdapter��holder�д洢ItemViews,���帳ֵ��Ҫʵ��renderData����
		 * 
		 * @param context
		 *            -----������
		 * @param listData
		 *            -----����Դ���������null���Զ�����listData 50���������ݣ������������У�key��id
		 *            int��imgResId int��col1-col10 String��
		 * @param itemLayoutID
		 *            -----item�����ļ�
		 * @param itemViewIDs
		 *            -----item Views ID
		 * 
		 */
		public MyGeneralBaseAdapter(Context context,
				List<? extends Object> listData, int itemLayoutID,
				int[] itemViewIDs)
		{
			this.context = context;
			this.listData = listData;
			this.itemLayoutID = itemLayoutID;
			this.itemViewIDs = itemViewIDs;
			getTestImgSysResIds();// ʹ�ò���ʼ���������� testImgSysResIds

			if (null == listData)
			{
				// ʹ�ò���ʼ���������� testListData
				this.listData = getTestListData();
			}

		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return listData.size();
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return listData.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{

			Holder holder;
			if (null == convertView)
			{
				convertView = ((LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(itemLayoutID, null);
				holder = new Holder();

				for (int itemID : itemViewIDs)
				{
					// ��Ӧ��ϵΪ��R.id, viewObject
					holder.setView(itemID, convertView.findViewById(itemID));
				}
			} else
			{
				holder = (Holder) convertView.getTag();
			}

			convertView.setTag(holder);

			// /////////////////////////////////////////////////////////////////////
			/***
			 * ����̳У���Ⱦ����
			 */
			this.renderData(position, holder);

			// //////////////////////////////////////////////////////////////////////

			return convertView;
		}

		public abstract void renderData(int position, Holder holder);

		class Holder
		{
			HashMap<Integer, View> mapView = new HashMap<Integer, View>();

			public void setView(int r_id, View v)
			{
				this.mapView.put(r_id, v);
			}

			@SuppressWarnings("unchecked")
			public <T> T getView(int r_id)
			{
				return (T) this.mapView.get(r_id);
			}

		}

		// ��������Դ
		public List<? extends Object> getListData()
		{
			return listData;
		}

		// //////���÷���
		/*
		 * MyGeneralBaseAdapterImp001(Context
		 * context,List<HashMap<String,Object>> listData,int itemLayoutID,int[]
		 * itemViewIDs) { //MyGeneralBaseAdapter
		 * super(context,listData,itemLayoutID,itemViewIDs);
		 * 
		 * }
		 * 
		 * @Override public void renderData(int position, Holder holder) {
		 * //R.id.tv999,R.id.image999 TextView tv = holder.getView(R.id.tv999);
		 * ImageView imageView = holder.getView(R.id.image999);
		 * 
		 * tv.setText(((HashMap<String,Object>)listData.get(position)).get("title"
		 * ).toString());
		 * imageView.setImageResource((Integer)((HashMap<String,Object
		 * >)listData.get(position)).get("src")); }
		 */

	}
	

	// //
	public static int[] getTestImgSysResIds()
	{
		// TODO Auto-generated method stub
		testImgSysResIds = new int[]
		{ android.R.drawable.ic_menu_add, android.R.drawable.ic_menu_agenda,
				android.R.drawable.ic_menu_always_landscape_portrait,
				android.R.drawable.ic_menu_call,
				android.R.drawable.ic_menu_camera,
				android.R.drawable.ic_menu_close_clear_cancel,
				android.R.drawable.ic_menu_compass,
				android.R.drawable.ic_menu_crop,
				android.R.drawable.ic_menu_day,
				android.R.drawable.ic_menu_delete,
				android.R.drawable.ic_menu_directions,
				android.R.drawable.ic_menu_edit,
				android.R.drawable.ic_menu_gallery,
				android.R.drawable.ic_menu_help,
				android.R.drawable.ic_menu_info_details,
				android.R.drawable.ic_menu_manage,
				android.R.drawable.ic_menu_mapmode,
				android.R.drawable.ic_menu_month,
				android.R.drawable.ic_menu_mylocation,
				android.R.drawable.ic_menu_myplaces,
				android.R.drawable.ic_menu_my_calendar,
				android.R.drawable.ic_menu_preferences,
				android.R.drawable.ic_menu_recent_history,
				android.R.drawable.ic_menu_report_image,
				android.R.drawable.ic_menu_revert,
				android.R.drawable.ic_menu_rotate,
				android.R.drawable.ic_menu_save,
				android.R.drawable.ic_menu_search,
				android.R.drawable.ic_menu_send,
				android.R.drawable.ic_menu_set_as,
				android.R.drawable.ic_menu_slideshow,
				android.R.drawable.ic_menu_sort_alphabetically,
				android.R.drawable.ic_menu_sort_by_size,
				android.R.drawable.ic_menu_today,
				android.R.drawable.ic_menu_upload,
				android.R.drawable.ic_menu_upload_you_tube,
				android.R.drawable.ic_menu_view,
				android.R.drawable.ic_menu_week,
				android.R.drawable.ic_menu_zoom,
				android.R.drawable.ic_menu_month };

		return testImgSysResIds;
	}

	// //���ɲ�������no��name
	public static List<HashMap<String, Object>> getTestListData()
	{
		testListData = new ArrayList<HashMap<String, Object>>();

		for (int i = 0; i < 50; i++)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", i);
			map.put("imgResId", testImgSysResIds[(int) (Math.random() * 40)]);
			map.put("col1", "col1:" + i);
			map.put("col2", "col2:" + i);
			map.put("col3", "col3:" + i);
			map.put("col4", "col4:" + i);
			map.put("col5", "col5:" + i);
			map.put("col6", "col6:" + i);
			map.put("col7", "col7:" + i);
			map.put("col8", "col8:" + i);
			map.put("col9", "col9:" + i);
			map.put("col10", "col10:" + i);

			testListData.add(map);
		}

		return testListData;
	}

	
	//�ж����繦���Ƿ����
	public static boolean getNetworkAvailable(Context ctx)
	{    
		ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);    
		NetworkInfo info = cm.getActiveNetworkInfo();    
		return (info != null && info.isConnected());
	}
	
	
	public static String getPackageVersionName(Context context)
	{
		String versionName="1.0";
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return versionName;
	}

	public static String getPackageVersionCode(Context context)
	{
		String versionCode="1.0";
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			//versionCode = ""+packageInfo.versionCode;
			versionCode = ""+packageInfo.versionName;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return versionCode;
	}
	
	public static class MySharedPreferencesGetSet
	{
		SharedPreferences sharedPreferences;
		public MySharedPreferencesGetSet(Context context)
		{
			sharedPreferences = context.getSharedPreferences("MySession", Context.MODE_PRIVATE);
		}
		public void setValue(String key,String value)
		{
			Editor editor = sharedPreferences.edit();
			editor.putString(key, value);
			editor.commit();
		}
		public String getValue(String key)
		{
			return sharedPreferences.getString(key,"MyDear");
		}
	}
	




	


	
	public static class FileUtil
	{
		//�õ���ǰ�ⲿ�洢�豸��Ŀ¼
		private static String SDPATH = Environment.getExternalStorageDirectory() + "/";
		
		public static String getSDPATH()
		{
			return SDPATH;
		}		
		
		//��SD���ϴ����ļ�
		public static File createSDFile(String fileName) throws IOException
		{
			File file = new File(SDPATH + fileName);
			file.createNewFile();
			return file;
		}
		
		//��SD���ϴ���Ŀ¼
		public static File createSDDir(String dirName)
		{
			File dir = new File(SDPATH + dirName);
			dir.mkdir();
			
			return dir;
		}
		
		
		//�ж�SD���ϵ��ļ����Ƿ����
		public static boolean isFileExist(String fileName)
		{
			File file = new File(SDPATH + fileName);
			return file.exists();
		}
		
		//��InputStreamд������SD����
		public static File writeInputStreamToSD(String path,String fileName,InputStream input)
		{
			File file = null;
			OutputStream output = null;
			
			try
			{
				createSDDir(path);
				file = createSDFile(path + fileName);
				output = new FileOutputStream(file);
				
				byte[] buffer = new byte[4*1024];
				
				int len;
				while((len = input.read(buffer)) != -1)
				{
					output.write(buffer,0,len);
					output.flush();
				}
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				try
				{
					output.close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return file;
		}
		
	
		
	}

	public static class HttpDownloadUtil
	{
		/*
		 * ����URL�����ļ��������ı�������ֵλ�ļ��ı�����
		 * 1������һ��URL����
		 * 2��ͨ��URL���󣬴���һ��HttpURLConnection����
		 * 3���õ�InputStream
		 * 4����InputStream���ж�ȡ����
		 */
		
		public static final String HttpDownloaderToString(String urlStr)
		{
			
			StringBuffer sb = new StringBuffer();
			String line = null;
			
			BufferedReader buffer = null;
			
			try
			{
				URL url = new URL(urlStr);
				HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
				buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				
				while((line = buffer.readLine()) != null)
				{
					sb.append(line);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					buffer.close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			return sb.toString();
		}
		
		
		public static final int HttpDownloaderToFile
		(String urlStr,String path,String fileName)
		{
			
			InputStream inputStream = null;
			
			try
			{
				
				if(FileUtil.isFileExist(path + fileName))
				{
					return 1;
				}
				else
				{
					URL url = new URL(urlStr);
					HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
					
					inputStream = urlConn.getInputStream();
					File file = FileUtil.writeInputStreamToSD(path, fileName, inputStream);
					if(file == null)
					{
						return -1;
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return -1;
			}
			
			
			return 0;
		}
		
	}


	
	public static String getVersionFromServer(Context context) throws Exception
	{
		String newVersion="0.0";
		String url = "http://218.31.79.219:16008/WL627/TestAction9990001";
		
		JSONObject jsonObjPost = new JSONObject();
		
		String condition;
			
		
		jsonObjPost.put("table_name", "dhhmgg_parameter");
		
		
		System.out.println("context.getPackageName()��"+context.getPackageName());
		
		condition = " para_type='app_version' and para_name='"+context.getPackageName()+"' ";
		
		jsonObjPost.put("table_condition", condition);
		
		System.out.println("table_condition:"+condition);
		
		
		JSONObject jsonObject = MyDear.HttpRequestUtil.GetJsonObjectByHttpPost(url, jsonObjPost);
		JSONArray jsonArray = (JSONArray)jsonObject.get("result");
		
		
		
		int size = jsonArray.length();
		
		System.out.println("jsonArray.length and size:>>>>>>>"+jsonArray.length() + "/" + size);
		
		if(size >0)
		{
			JSONObject o = (JSONObject)jsonArray.get(0);
			System.out.println(o.getString("col1") + "/" + o.getString("col2") + "/" +  o.getString("col3")+ "/" + o.getString("col4"))  ;
		
			newVersion = o.getString("col3");
			
			mapParameter.put("newVersion", newVersion);
			mapParameter.put("newVersionInfo", o.getString("col4"));
		}
		
		return newVersion;
	}
	
	
	public static final void apkInstall(String apkUrl,Activity activity)
	{
		Uri uri = Uri.fromFile(new File(apkUrl));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(intent);
		
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public static final boolean fileOnlineDownloadAndStore(final String url,String sdPath, String fileName)
	{
		
				//String url = "http://218.31.79.219:16008/tool/gg_base_0001.apk";
			
				boolean isOK = false;
				byte[] data = MyDear.HttpRequestUtil.GetByteObjectByHttpGet(url);
				
				FileOutputStream outputStream = null;
				if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
				{

					try
					{
						outputStream = new FileOutputStream(new File(sdPath,fileName));
						outputStream.write(data);
						isOK = true;
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(outputStream != null)
					{
						try
						{
							outputStream.close();
						} catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				return isOK;
	}
	

	public static class AsynTaskRequestUtil
	{
		static class MyAsynTaskGetBitmap extends AsyncTask<String, Void, Bitmap>
		{

			private CallBack callback;
			MyAsynTaskGetBitmap(CallBack callback)
			{
				this.callback = callback;
			}
			@Override
			protected Bitmap doInBackground(String... params)
			{
				// TODO Auto-generated method stub
				
				byte[] data = MyDear.HttpRequestUtil.GetByteObjectByHttpGet(params[0]);
				System.out.println("params[0]:"+params[0]);
				System.out.println("data.data:"+data);
				if(null != data)
				{
					System.out.println("null http request");
					return BitmapFactory.decodeByteArray(data, 0, data.length);
				}
				else
					return null;
			}
			
			@Override
			protected void onPostExecute(Bitmap result)
			{
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				callback.callbackInvokeForResult(result, new String[]{"SUCCESS"});
			}
		}
		
		static class MyAsynTaskGetJsonObject extends AsyncTask<String, Void, JSONObject>
		{

			private CallBack callback;
			private JSONObject jsonObjectSend;
			
			MyAsynTaskGetJsonObject(JSONObject jsonObjectSend ,CallBack callback)
			{
				this.jsonObjectSend = jsonObjectSend;
				this.callback = callback;
			}
			@Override
			protected JSONObject doInBackground(String... params)
			{
				// TODO Auto-generated method stub
				
				JSONObject data = MyDear.HttpRequestUtil.GetJsonObjectByHttpPost(params[0],jsonObjectSend);
				return data;
			}
			
			@Override
			protected void onPostExecute(JSONObject result)
			{
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				callback.callbackInvokeForResult(result, new String[]{"SUCCESS"});
			}
		}
		
		static class MyAsynTaskGetStringObject extends AsyncTask<String, Void, String>
		{

			private CallBack callback;
			
			MyAsynTaskGetStringObject(CallBack callback)
			{
				this.callback = callback;
			}
			@Override
			protected String doInBackground(String... params)
			{
				// TODO Auto-generated method stub
				
				String data = MyDear.HttpRequestUtil.GetStringObjectByHttpGet(params[0]);
				return data;
			}
			
			@Override
			protected void onPostExecute(String result)
			{
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				callback.callbackInvokeForResult(result, new String[]{"SUCCESS"});
			}
		}
		
	}
	
	
	public static class AsynHttpRequestUtil
	{
		
		static void AsynHttpRequestDoPostForJsonObject(final String actionUrl,final JSONObject jsonObjSendPara,final CallBack callback)
		{
			MyThreadPoolExecutor.execute(new Runnable()
			{
				@Override
				public void run()
				{
					HttpRequestUtil.GetJsonObjectByHttpPostCallBack(actionUrl, jsonObjSendPara,callback);
				}
			});
		}
		
		static void AsynHttpRequestDoGetForByte(final String url,final CallBack callback)
		{
			MyThreadPoolExecutor.execute(new Runnable()
			{
				@Override
				public void run()
				{
					HttpRequestUtil.GetByteObjectByHttpGetCallBack(url, callback);
				}
			});
		}
	}
	
	
	
	public static class HttpRequestUtil
	{
		public static JSONObject GetJsonObjectByHttpPostCallBack(String disUrl,
				JSONObject jsonObjSend,CallBack callback)
		{
			
			// �������Null����ʾ�������������쳣
			JSONObject jsonObjRsp = null;

			try
			{
				jsonObjRsp = new JSONObject("{result:[]}");
			} catch (JSONException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


			try
			{
				// ���ݴ��ݹ�����JSONObject�����������
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				Iterator<?> iterator = jsonObjSend.keys();
				String key, value;
				while (iterator.hasNext())
				{
					key = (String) iterator.next();
					value = jsonObjSend.getString(key);
					nameValuePairs.add(new BasicNameValuePair(key, value));
				}

				StringBuilder builder = new StringBuilder();
				HttpPost httpPost = new HttpPost(disUrl);
				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded; charset=utf-8"); // ��Ҫ

				// httpPost.setEntity(new StringEntity(jsonObjSend.toString()));
				// //�������˻�ȡ����������һֱΪnull����
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));

				// ---��������timeout------------------------
				BasicHttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams,
						REQUEST_TIMEOUT);
				HttpConnectionParams.setSoTimeout(httpParams, WAITDATA_TIMEOUT); // so,socked
																					// for
																					// data
				// ---------------------------
				HttpClient httpClient = new DefaultHttpClient(httpParams);
				// --------------------------------------------------

				HttpResponse response = httpClient.execute(httpPost);

				// ------------------------------------
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				{
					server_response_is_normal = true;
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(response.getEntity().getContent()));
					for (String s = reader.readLine(); s != null; s = reader
							.readLine())
					{
						builder.append(s);
					}
					jsonObjRsp = new JSONObject(builder.toString());
					
					callback.callbackInvokeForResult(jsonObjRsp, new String[]{"SUCCESS"});
					
				}
				else
				{
					System.out.println("request ErrorEEEEEEEEEEEEEE");
				}
				

				// ------------------------------------
			} catch (Exception e)
			{
				server_response_is_normal = false;
				return jsonObjRsp;
			}

			return jsonObjRsp;
		}
		
		



		public static byte[] GetByteObjectByHttpGetCallBack(String disUrl,CallBack callback)
		{
			
			
			byte[] data = null;
			
		        HttpGet httpGet = new HttpGet(disUrl);
		        HttpClient httpClient = new DefaultHttpClient();
				HttpResponse response;
				try
				{
					response = httpClient.execute(httpGet);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
					{  
						server_response_is_normal = true;
						data = EntityUtils.toByteArray(response.getEntity());
						callback.callbackInvokeForResult(data, new String[]{"SUCCESS"});
						return data;
		            } 
				} catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
	    	
			return data;
		}
	
	
		//�������Null����ʾ�������������쳣
		
		/*
		 * MyDear.HttpRequestUtil httpRequestUtil = new MyDear.HttpRequestUtil();
		 * String url = "http://218.31.79.219:16008/WL627/TestAction9990001"; //"http://218.31.79.219:16008/WL627/resdatatojson.jsp";
		 * String param = "col1=123a&col2=456b&col3=789c";
		 */
		public static JSONObject GetJsonObjectByHttpGet(String url, String param)
		{
			JSONObject jsonObjRsp=null;
			
			try
			{
				jsonObjRsp = new JSONObject("{result:[]}");
			} catch (JSONException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(null == jsonObjRsp)jsonObjRsp = new JSONObject();
			
			try
			{
				 StringBuilder strBuilder = new StringBuilder();
				 String urlRequest = url + "?" + param;
				 
				 HttpGet httpGet = new HttpGet(urlRequest);
				 httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");//��Ҫ
				 
				//---��������timeout------------------------
			        BasicHttpParams httpParams = new BasicHttpParams();
			        HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
			        HttpConnectionParams.setSoTimeout(httpParams, WAITDATA_TIMEOUT); //so,socked for data
			    //---------------------------
			        HttpClient httpClient = new DefaultHttpClient(httpParams);
			    //--------------------------------------------------
			        
				 
				 HttpResponse response = httpClient.execute(httpGet);
				 
				 
				//------------------------------------
					
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
				{  
						server_response_is_normal = true;
		        } 
					
				//------------------------------------
					
				 
				 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				 
				 for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) 
				 {
					 strBuilder.append(s);
				 }
				 jsonObjRsp = new JSONObject(strBuilder.toString());
					
			}
			catch(Exception e)
			{
				server_response_is_normal = false;
				return jsonObjRsp;
			}
			
			return jsonObjRsp;
		}
		
		/*
		 * MyDear.HttpRequestUtil httpRequestUtil = new MyDear.HttpRequestUtil();
		 * String url = "http://218.31.79.219:16008/WL627/TestAction9990001"; //"http://218.31.79.219:16008/WL627/resdatatojson.jsp";
		 * JSONObject jsonObjSend = new JSONObject(str);   String str = '{"col1":"col1Put","col3":"col3Put","col2":"�������"}'
		 */
		public static JSONObject GetJsonObjectByHttpPost(String disUrl, JSONObject jsonObjSend)
		{
			
			
			if(null == jsonObjSend)jsonObjSend=new JSONObject();
			//�������Null����ʾ�������������쳣
			
			JSONObject jsonObjRsp=null;
			
			try
			{
				jsonObjRsp = new JSONObject("{result:[]}");
			} catch (JSONException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(null == jsonObjRsp)jsonObjRsp = new JSONObject();
			
			
			
	    	try {
	    		
	            //���ݴ��ݹ�����JSONObject�����������              
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	            Iterator<?> iterator = jsonObjSend.keys();
	            String key,value;
	            while(iterator.hasNext())
	            {
	            	key   = (String)iterator.next();
	            	value = jsonObjSend.getString(key);
	            	nameValuePairs.add(new BasicNameValuePair(key, value));
	            }
	            
				StringBuilder builder = new StringBuilder();
		        HttpPost httpPost = new HttpPost(disUrl);
		        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"); //��Ҫ

		        //httpPost.setEntity(new StringEntity(jsonObjSend.toString()));  //�������˻�ȡ����������һֱΪnull����
		        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs ,HTTP.UTF_8));
	            
		        //---��������timeout------------------------
		        BasicHttpParams httpParams = new BasicHttpParams();
		        HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		        HttpConnectionParams.setSoTimeout(httpParams, WAITDATA_TIMEOUT); //so,socked for data
		        //---------------------------
		        HttpClient httpClient = new DefaultHttpClient(httpParams);
		        //--------------------------------------------------
		        
				HttpResponse response = httpClient.execute(httpPost);
				
				//------------------------------------
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
				{  
					server_response_is_normal = true;
					BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					for (String s = reader.readLine(); s != null; s = reader.readLine()) {
						builder.append(s);
					}
					jsonObjRsp = new JSONObject(builder.toString());
	            } 
				//------------------------------------
			} 
	    	catch (Exception e) 
			{
				server_response_is_normal = false;
				return jsonObjRsp;
			}
	    	
			return jsonObjRsp;
		}
		
		public static byte[] GetByteObjectByHttpGet(String disUrl)
		{
			
			System.out.println("being 1111");
			System.out.println(disUrl);
			
			byte[] data = null;
			
		        HttpGet httpGet = new HttpGet(disUrl);
		        HttpClient httpClient = new DefaultHttpClient();
				HttpResponse response;
				try
				{
					System.out.println("being 2222");
					
					response = httpClient.execute(httpGet);
					
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
					{  
						System.out.println("being 3333");
						
						server_response_is_normal = true;
						data = EntityUtils.toByteArray(response.getEntity());
						
						System.out.println("data: getbytes:"+data.length);
						return data;
		            } 
					
					System.out.println("being 444");
					
				} catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
	    	
			return data;
		}
		
		public static String GetStringObjectByHttpGet(String disUrl)
		{
			
			
			String data = null;
			
		        HttpGet httpGet = new HttpGet(disUrl);
		        HttpClient httpClient = new DefaultHttpClient();
				HttpResponse response;
				try
				{
					response = httpClient.execute(httpGet);
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
					{  
						server_response_is_normal = true;
						data = EntityUtils.toString(response.getEntity());
						return data;
		            } 
				} catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
	    	
			return data;
		}
	}

	
	
	
	
	public static final void toastShow(Context context,String str)
	{
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}
	
	public static final void sysPrint(Object o)
	{
		System.out.println(o);
	}
	
	public static final void log(String str)
	{
		Log.i("MyDear",str);
	}
	
	public static final ArrayList<Map<String,Object>> convertCursorToList(Cursor cursor,String[] columns)
	{
		ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String,Object>>();  
        //����Cursor�����   
        while(cursor.moveToNext()){  
            //����������е����ݴ���Arraylist��   
            Map<String,Object> map = new HashMap<String, Object>();
            
            //��ȡһ����¼
            for(int i=0; i < columns.length; i++)
            {
            	map.put(columns[i], cursor.getString(i));
            }
            
            arrayList.add(map);
        }
		return arrayList;
	}
	
	public static final List<HashMap<String,Object>> arrayListMapFactory()
	{
		return new ArrayList<HashMap<String,Object>>();
	}
	
	
	public static final class MySqliteOpenHelper extends SQLiteOpenHelper
	{

		//new MyDear.MySqliteOpenHelper(this,"mydearDB",null,1);
		
		public MySqliteOpenHelper(Context context, String name,
				CursorFactory factory, int version)
		{
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		public MySqliteOpenHelper(Context context, String name,
				CursorFactory factory, int version,
				DatabaseErrorHandler errorHandler)
		{
			super(context, name, factory, version, errorHandler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// TODO Auto-generated method stub
			MyDear.log("on db create exc");
			db.execSQL("create table dhhmgg(col1,col2,col3,col4,col5);");
			db.execSQL("create table dhhmgg2(col1 integer primary key autoincrement,"
					+ "col2,col3,col4,col5,col6,col7,col8,col9);");
			db.execSQL("create table dhhmgg9(_ID integer primary key autoincrement,"
					+ "col2,col3,col4,col5,col6,col7,col8,col9);");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			// TODO Auto-generated method stub
			MyDear.log("on db onUpgrade exc");
			
			String sql = "drop table if exists ";
			db.execSQL(sql);
			this.onCreate(db);
		}

	}
	
	

	

	
	
	public static class FileItem
	{
		String fileName;
		String createDate;
		String path;
		long  createDateLong;
	}
	
	/**
	 * �����ض���׺���Ƶ��ļ�������ֵlistFile�б�
	 * 
	 * use-detail File file = new File(Environment.getExternalStorageDirectory()+"/DCIM");
				List<FileItem> listFile = util.getiterateFileBySuffixToList(file, "jpg");\
				
	 * @param file      ----Ŀ¼
	 * @param suffix    ----��׺��
	 * @return
	 */
	public static List<FileItem> MyGetiterateFileBySuffixToList(File file,String suffix)
	{
		List<FileItem> listFile = new ArrayList<FileItem>();
		
		iterateFileBySuffixToList(file,listFile,suffix);
		
		
		Collections.sort(listFile, new Comparator<FileItem>()
		{

			@Override
			public int compare(FileItem lhs, FileItem rhs)
			{
				// TODO Auto-generated method stub
				if(lhs.createDateLong > rhs.createDateLong )
				{
					return -1;
				}
				return 1;
			}
			 
		});
		
		return listFile;
	}
	//���ݺ�׺���������ļ���Ȼ�����list�б�
	public static void iterateFileBySuffixToList(File file,List<FileItem> listFile,String suffix)
	{
	    
			if(file.listFiles()!=null)//�ļ�Ŀ����赽���ļ�����Д�
			{
				for( File filex : file.listFiles())
					{
					if(filex.isDirectory())
					{
						iterateFileBySuffixToList(filex,listFile,suffix);
					}
					else
					{
						String filename=filex.getName();
						String createDate= new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date(filex.lastModified()));
						Long createDateLong = new Date(filex.lastModified()).getTime();
						String path=filex.getAbsolutePath();
						long length=0;
						try {
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//��׺�Ĵ�Сд
						//if(filename.endsWith(".jpg") || filename.endsWith(".JPG") )
						if(filename.endsWith(suffix.toLowerCase()) || filename.endsWith(suffix.toUpperCase()) )
						{
							FileItem fileItem=new FileItem();
							fileItem.fileName=filename;
							fileItem.createDate=createDate;
							fileItem.path=path;
							fileItem.createDateLong = createDateLong;
							listFile.add(fileItem);
						}
					}				
			   };		
		}
	}

	

	
	public static class MyUploadFileToServer
	{
		/**
		 * use-detial:  UploadFileToServer uploadToServer = new UploadFileToServer();
		 *              uploadToServer.uploadToServer.uploadFile(new File(sendFile.path));
		 *	�ļ��ľ���·��              
		 *              
		 * �ϴ�ͼƬ��������
		 * 
		 * @param imageFile
		 *            ����·��
		 */
		String TAG = "hello";
	
		public void uploadFile(File file)
		{
			try
			{
				String requestUrl = "http://218.31.79.219:16008/WL627/execute";
	
				// ������ͨ��Ϣ
				Map<String, String> params = new HashMap<String, String>();
				params.put("fileName", file.getName());
				// �ϴ��ļ�
				FormFile formfile = new FormFile(file.getName(), file,"image", "application/octet-stream");
	
				SocketHttpRequester.post(requestUrl, params, formfile);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	
		/**
		 * �ϴ��ļ�
		 */
		public static class FormFile
		{
			/* �ϴ��ļ������� */
			private byte[] data;
			private InputStream inStream;
			private File file;
			/* �ļ����� */
			private String filname;
			/* ����������� */
			private String parameterName;
			/* �������� */
			private String contentType = "application/octet-stream";
	
			public FormFile(String filname, byte[] data, String parameterName,
					String contentType)
			{
				this.data = data;
				this.filname = filname;
				this.parameterName = parameterName;
				if (contentType != null)
					this.contentType = contentType;
			}
	
			public FormFile(String filname, File file, String parameterName,
					String contentType)
			{
				this.filname = filname;
				this.parameterName = parameterName;
				this.file = file;
				try
				{
					this.inStream = new FileInputStream(file);
				} catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				if (contentType != null)
					this.contentType = contentType;
			}
	
			public File getFile()
			{
				return file;
			}
	
			public InputStream getInStream()
			{
				return inStream;
			}
	
			public byte[] getData()
			{
				return data;
			}
	
			public String getFilname()
			{
				return filname;
			}
	
			public void setFilname(String filname)
			{
				this.filname = filname;
			}
	
			public String getParameterName()
			{
				return parameterName;
			}
	
			public void setParameterName(String parameterName)
			{
				this.parameterName = parameterName;
			}
	
			public String getContentType()
			{
				return contentType;
			}
	
			public void setContentType(String contentType)
			{
				this.contentType = contentType;
			}
	
		}
	
		/**
		 * �ϴ��ļ���������
		 * 
		 * @author Administrator
		 *
		 */
		public static class SocketHttpRequester
		{
			/**
			 * ֱ��ͨ��HTTPЭ���ύ���ݵ�������,ʵ����������ύ����: <FORM METHOD=POST
			 * ACTION="http://192.168.1.101:8083/upload/servlet/UploadServlet"
			 * enctype="multipart/form-data"> <INPUT TYPE="text" NAME="name"> <INPUT
			 * TYPE="text" NAME="id"> <input type="file" name="imagefile"/> <input
			 * type="file" name="zip"/> </FORM>
			 * 
			 * @param path
			 *            �ϴ�·��(ע������ʹ��localhost��127.0.0.1������·�����ԣ���Ϊ����ָ���ֻ�ģ������
			 *            �����ʹ��http://www.iteye.cn��http://192.168.1.101:8083������·������)
			 * @param params
			 *            ������� keyΪ������,valueΪ����ֵ
			 * @param file
			 *            �ϴ��ļ�
			 */
			public static boolean post(String path, Map<String, String> params,
					FormFile[] files) throws Exception
			{
				final String BOUNDARY = "---------------------------7da2137580612"; // ���ݷָ���
				final String endline = "--" + BOUNDARY + "--\r\n";// ���ݽ�����־
	
				int fileDataLength = 0;
				for (FormFile uploadFile : files)
				{// �õ��ļ��������ݵ��ܳ���
					StringBuilder fileExplain = new StringBuilder();
					fileExplain.append("--");
					fileExplain.append(BOUNDARY);
					fileExplain.append("\r\n");
					fileExplain.append("Content-Disposition: form-data;name=\""
							+ uploadFile.getParameterName() + "\";filename=\""
							+ uploadFile.getFilname() + "\"\r\n");
					fileExplain.append("Content-Type: "
							+ uploadFile.getContentType() + "\r\n\r\n");
					fileExplain.append("\r\n");
					fileDataLength += fileExplain.length();
					if (uploadFile.getInStream() != null)
					{
						fileDataLength += uploadFile.getFile().length();
					} else
					{
						fileDataLength += uploadFile.getData().length;
					}
				}
				StringBuilder textEntity = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet())
				{// �����ı����Ͳ�����ʵ������
					textEntity.append("--");
					textEntity.append(BOUNDARY);
					textEntity.append("\r\n");
					textEntity.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"\r\n\r\n");
					textEntity.append(entry.getValue());
					textEntity.append("\r\n");
				}
				// ���㴫�����������ʵ�������ܳ���
				int dataLength = textEntity.toString().getBytes().length
						+ fileDataLength + endline.getBytes().length;
	
				URL url = new URL(path);
				int port = url.getPort() == -1 ? 80 : url.getPort();
				Socket socket = new Socket(InetAddress.getByName(url.getHost()),
						port);
				OutputStream outStream = socket.getOutputStream();
				// �������HTTP����ͷ�ķ���
				String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
				outStream.write(requestmethod.getBytes());
				String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
				outStream.write(accept.getBytes());
				String language = "Accept-Language: zh-CN\r\n";
				outStream.write(language.getBytes());
				String contenttype = "Content-Type: multipart/form-data; boundary="
						+ BOUNDARY + "\r\n";
				outStream.write(contenttype.getBytes());
				String contentlength = "Content-Length: " + dataLength + "\r\n";
				outStream.write(contentlength.getBytes());
				String alive = "Connection: Keep-Alive\r\n";
				outStream.write(alive.getBytes());
				String host = "Host: " + url.getHost() + ":" + port + "\r\n";
				outStream.write(host.getBytes());
				// д��HTTP����ͷ�����HTTPЭ����дһ���س�����
				outStream.write("\r\n".getBytes());
				// �������ı����͵�ʵ�����ݷ��ͳ���
				outStream.write(textEntity.toString().getBytes());
				// �������ļ����͵�ʵ�����ݷ��ͳ���
				for (FormFile uploadFile : files)
				{
					StringBuilder fileEntity = new StringBuilder();
					fileEntity.append("--");
					fileEntity.append(BOUNDARY);
					fileEntity.append("\r\n");
					fileEntity.append("Content-Disposition: form-data;name=\""
							+ uploadFile.getParameterName() + "\";filename=\""
							+ uploadFile.getFilname() + "\"\r\n");
					fileEntity.append("Content-Type: "
							+ uploadFile.getContentType() + "\r\n\r\n");
					outStream.write(fileEntity.toString().getBytes());
					if (uploadFile.getInStream() != null)
					{
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = uploadFile.getInStream()
								.read(buffer, 0, 1024)) != -1)
						{
							outStream.write(buffer, 0, len);
						}
						uploadFile.getInStream().close();
					} else
					{
						outStream.write(uploadFile.getData(), 0,
								uploadFile.getData().length);
					}
					outStream.write("\r\n".getBytes());
				}
				// ���淢�����ݽ�����־����ʾ�����Ѿ�����
				outStream.write(endline.getBytes());
	
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				if (reader.readLine().indexOf("200") == -1)
				{// ��ȡweb���������ص����ݣ��ж��������Ƿ�Ϊ200���������200����������ʧ��
					return false;
				}
				outStream.flush();
				outStream.close();
				reader.close();
				socket.close();
				return true;
			}
	
			/**
			 * �ύ���ݵ�������
			 * 
			 * @param path
			 *            �ϴ�·��(ע������ʹ��localhost��127.0.0.1������·�����ԣ���Ϊ����ָ���ֻ�ģ������
			 *            �����ʹ��http://www.itcast.cn��http://192.168.1.10:8080������·������)
			 * @param params
			 *            ������� keyΪ������,valueΪ����ֵ
			 * @param file
			 *            �ϴ��ļ�
			 */
			public static boolean post(String path, Map<String, String> params,
					FormFile file) throws Exception
			{
				return post(path, params, new FormFile[] { file });
			}
		}
	}
	
	

	public static class MyThreadPoolExecutor
	{
		//corePoolSize�� �̳߳�ά���̵߳���������
		//maximumPoolSize���̳߳�ά���̵߳��������
		//keepAliveTime�� �̳߳�ά���߳�������Ŀ���ʱ��
		//unit�� �̳߳�ά���߳�������Ŀ���ʱ��ĵ�λ
		//workQueue�� �̳߳���ʹ�õĻ������
		//handler�� �̳߳ضԾܾ�����Ĵ������

		// ��������߳����������߳�
		private static int CORE_POOL_SIZE = 20;
		// �̳߳�����߳����������������е��̶߳��Ᵽ����ٸ���
		private static int MAX_POOL_SIZE = 100;
		// �����߳̿���״̬����ʱ��
		private static int KEEP_ALIVE_TIME = 5000;
		//��ʼ��������
		private static int BlockingQueueCapacity = 5;
		
		// �̳߳�ThreadPoolExecutor java�Դ����̳߳�
		public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
					KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(BlockingQueueCapacity),  
	                new ThreadPoolExecutor.DiscardOldestPolicy());
		static
		{
			// allowCoreThreadTimeOut true,����ִ����Ϻ�ر�ȫ���̣߳�����һֱѭ��
			// shutdown();��ر�����ʵ�������ڲ�����ִ������
			threadPoolExecutor.allowCoreThreadTimeOut(true);
		}
		

		public static void execute(Runnable runnable) 
		{
			threadPoolExecutor.execute(runnable);
		}
		public static void remove(Runnable runnable)
		{
			threadPoolExecutor.remove(runnable);
		}
	}
	
}
