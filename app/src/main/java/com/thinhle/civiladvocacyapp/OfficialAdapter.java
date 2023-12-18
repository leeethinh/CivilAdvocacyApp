package com.thinhle.civiladvocacyapp;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewholder>{
    private ArrayList<Official> officialList;
    private MainActivity mainActivity;
    private Picasso picasso;

    public OfficialAdapter(ArrayList<Official> officialList, MainActivity mainActivity){
        this.officialList = officialList;
        this.mainActivity = mainActivity;
    }
    public OfficialViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        picasso = Picasso.get();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_list_entry,parent,false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new OfficialViewholder(itemView);
    }
    public void onBindViewHolder(@NonNull OfficialViewholder holder, int position) {
        Official official = officialList.get(position);
        holder.name.setText(official.getName()+" ("+official.getParty()+")");
        holder.office.setText(official.getOffice());
        String imageUrl = official.getImageID();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            picasso.load(imageUrl).error(R.drawable.brokenimage).into(holder.image);
        } else {
            // Handle the case when the image URL is empty or null
            holder.image.setImageResource(R.drawable.missing);
        }



    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}

