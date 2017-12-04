package com.gg.ad_test_chabaike_11001;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.gg.ad_test_chabaike_11001.MyDear.CallBack;
import com.gg.ad_test_chabaike_11001.MyDear.MyGeneralBaseAdapter;


public class MyGeneralAdapterFoodMain extends MyGeneralBaseAdapter
{

	//»º´æÍ¼Æ¬
	HashMap<Integer,Bitmap> bitmaps = new HashMap<Integer,Bitmap>();
	public MyGeneralAdapterFoodMain(Context context,
			List<? extends Object> listData, int itemLayoutID,
			int[] itemViewIDs)
	{
		super(context, listData, itemLayoutID, itemViewIDs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void renderData(final int position, Holder holder)
	{
		// TODO Auto-generated method stub
		
		final ImageView image = holder.getView(R.id.pic);
		TextView  title = holder.getView(R.id.title);
		TextView  food_str = holder.getView(R.id.food_str);
		TextView  num = holder.getView(R.id.num);
		
		Food food = (Food) listData.get(position);
		title.setText(food.getTitle());
		food_str.setText(food.getFood_str());
		num.setText(""+food.getNum());
		
		if(null != bitmaps.get(position))
		{
			image.setImageBitmap(bitmaps.get(position));
		}
		else
		{
			new MyDear.AsynTaskRequestUtil.MyAsynTaskGetBitmap(new CallBack(){

				@Override
				public void callbackInvokeForResult(Object o, String... strs)
				{
					
					Bitmap bitmap = (Bitmap) o;
					image.setImageBitmap(bitmap);
					
					bitmaps.put(position, bitmap);
				}}).execute(food.getPic());
		}


	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
