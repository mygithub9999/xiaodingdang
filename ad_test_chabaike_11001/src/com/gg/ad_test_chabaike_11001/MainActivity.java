package com.gg.ad_test_chabaike_11001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity
{
	ListView mylistView;
	List<Food> listData = new ArrayList<Food>();
	
	MyGeneralAdapterFoodMain myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mylistView = (ListView) findViewById(R.id.mylistView);
		myAdapter = new MyGeneralAdapterFoodMain(this.getApplicationContext(),
				listData,
				R.layout.item_listview_0004,
				new int[]{R.id.pic,R.id.title,R.id.food_str,R.id.num});
		
		mylistView.setAdapter(myAdapter);
		
		loadData();
		
	}

	private void loadData()
	{
		// TODO Auto-generated method stub
		new MyDear.AsynTaskRequestUtil.MyAsynTaskGetJsonObject(null, new MyDear.CallBack(){

			@Override
			public void callbackInvokeForResult(Object o, String... strs)
			{
				try
				{
					JSONArray jsonArray = ((JSONObject)o).getJSONArray("data");
					
					
					for(int i=0; i<jsonArray.length(); i++)
					{
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						Food food = new Food();
						food.setTitle(jsonObject.getString("title"));
						food.setPic(jsonObject.getString("pic"));
						food.setFood_str(jsonObject.getString("food_str"));
						food.setNum(jsonObject.getInt("num"));
						
						listData.add(food);
					}
					
					myAdapter.notifyDataSetChanged();
					
				} catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}}).execute("http://www.qubaobei.com/ios/cf/dish_list.php?stage_id=1&page=1&limit=20");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
