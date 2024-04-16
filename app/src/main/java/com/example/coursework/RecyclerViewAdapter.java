package com.example.coursework;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

//This class binds data to the recycler view
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    //Declare the variables
    private final String[] mCocktailList;
    private final String[] mCocktailIds;
    private final Context mContext;
    public boolean isClickable = true;

    //Initialise tag for logging
    private static final String TAG = "RecyclerViewAdapter";

    //Setting the variables for the adapter
    public RecyclerViewAdapter(String[] cocktailList, String[] cocktailIds, Context context){
        //Log message for RecyclerViewAdapter
        Log.i(TAG, "RecyclerViewAdapter");

        mCocktailList = cocktailList;
        mCocktailIds = cocktailIds;
        mContext = context;

        //If there is an io error then cocktailId will be 0 so item cannot be clickable
        if (mCocktailIds[0].equals("0")) {
            isClickable = false;
        }
    }

    //Creates a view holder and returns it
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //Log message for onCreateViewHolder
        Log.i(TAG, "onCreateViewHolder");

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cocktail_list_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view, this);
        return holder;
    }

    //Bind the data with the view holder
    public void onBindViewHolder(ViewHolder holder, int position){
        //Log message for onBindViewHolder
        Log.i(TAG, "onBindViewHolder");

        String mCurrent = mCocktailList[position];
        holder.cocktailName.setText(mCurrent);
    }

    //Provides the number of items to be displayed in the recycler view
    public int getItemCount() {
        return mCocktailList.length;
    }

    //Create the ViewHolder class to populate each item of the recycler view
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Initialise tag for logging
        private static final String TAG = "ViewHolder";

        //Declare the variables with the objects
        TextView cocktailName;
        ImageView itemImage;

        //Inflate the layout and set a click listener
        public ViewHolder(View itemView, RecyclerViewAdapter recyclerViewAdapter) {
            super(itemView);

            //Log message for ViewHolder
            Log.i(TAG, "ViewHolder");

            cocktailName = (TextView) itemView.findViewById(R.id.cocktail_name);
            itemImage = itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(new View.OnClickListener() {

                //Item will be non clickable if reporting an i/o error
                @Override public void onClick(View v) {
                    if(!isClickable){
                        return;
                    }
                    int position = getAdapterPosition();
                    showWebPage(v, mCocktailIds[position]);
                }
            });
        }
        @Override
        public void onClick(View v) {}
    }

    //Implicit intent to display webpage
    public void showWebPage(View view, String id) {
        //Log message for showWebPage
        Log.i(TAG, "showWebPage");

        String url = "https://www.thecocktaildb.com/drink/" + id;
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        view.getContext().startActivity(intent);
    }
}
