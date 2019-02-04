package cz.folprechtova.hides.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.folprechtova.hides.R;
import cz.folprechtova.hides.dto.Hide;
import cz.folprechtova.hides.utils.FakeDataBuilder;
import cz.folprechtova.hides.utils.JSONUtils;

public class ListActivity extends BaseBackButtonActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        /**
         TAKTO BYCHOM MOHLI VOLAT Z API, z Firebase
         MY SI MÍSTO TOHO DATA VEZMEME Z FAKE BUILDERU

        RestClient.get().getHides("http://edu.uhk.cz/~folprle1/posedy/").enqueue(new Callback<List<Hide>>() {
            @Override
            public void onResponse(Call<List<Hide>> call, Response<List<Hide>> response) {
                List<Hide> body = response.body();
                setupAdapters(body);
            }

            @Override
            public void onFailure(Call<List<Hide>> call, Throwable t) {
                System.out.println(t);
            }
        });
         */

        //s použitím GSON knihovny přepošleme do metody typ třídy ve tvaru Class[].class
        List<Hide> list = JSONUtils.getListFromJson(FakeDataBuilder.FAKE, Hide[].class);

        setupAdapters(list);

    }

    private void setupAdapters(List<Hide> list){
        HideAdapter hideAdapter = new HideAdapter(list);
        recyclerView.setAdapter(hideAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //adaptér k recycler view
    class HideAdapter extends RecyclerView.Adapter<HideAdapter.ViewHolder>{

        private List<Hide> items;

        public HideAdapter(List<Hide> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hide_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.hideTitleTextView.setText(items.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return items == null? 0 : items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

            TextView hideTitleTextView;

            public ViewHolder(View itemView) {
                super(itemView);

                hideTitleTextView = ((TextView) itemView.findViewById(R.id.hideTitleTextView));

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, HideDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("HIDE", items.get(getLayoutPosition()));
                intent.putExtras(bundle);
                startActivity(intent); //díky tomu že Hide i Comment implementují Serializable, lze je narvat do bundles to intentu a v jiné aktivitě si je vzít
            }

            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(ListActivity.this, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("HIDE", items.get(getLayoutPosition()));
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        }
    }
}
