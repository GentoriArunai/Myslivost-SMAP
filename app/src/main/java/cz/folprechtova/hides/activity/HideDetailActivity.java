package cz.folprechtova.hides.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.folprechtova.hides.R;
import cz.folprechtova.hides.dto.Comment;
import cz.folprechtova.hides.dto.Hide;
import cz.folprechtova.hides.utils.DialogHelper;
import cz.folprechtova.hides.utils.Preferences;
import cz.folprechtova.hides.web.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HideDetailActivity extends BaseBackButtonActivity {

    private LinearLayoutManager linearLayoutManager;
    private CommentAdapter commentAdapter;
    private RecyclerView recyclerView;
    private Hide hide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hide_detail);

        hide = (Hide) getIntent().getExtras().getSerializable("HIDE"); //natažení serializable z intentu

        TextView titleStand = (TextView) findViewById(R.id.titleStand);
        titleStand.setText(hide.getName());

        final TextView textOccupied = (TextView) findViewById(R.id.textOccupied);
        final CheckedTextView ctvSetOccupied = (CheckedTextView) findViewById(R.id.textSetOccupied);

        if (hide.isOccupied()) {
            textOccupied.setText(getString(R.string.occupied));
            textOccupied.setTextColor(ContextCompat.getColor(this, R.color.redText));
            ctvSetOccupied.setEnabled(false);
        } else {
            textOccupied.setText(getString(R.string.notOccupied));
            textOccupied.setTextColor(ContextCompat.getColor(this, R.color.greenText));
            ctvSetOccupied.setEnabled(true);
            ctvSetOccupied.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ctvSetOccupied.isChecked()) {
                        ctvSetOccupied.setChecked(false);
                        textOccupied.setText(getString(R.string.notOccupied));
                        textOccupied.setTextColor(ContextCompat.getColor(HideDetailActivity.this, R.color.greenText));
                        setOccupied(false);
                    } else {
                        ctvSetOccupied.setChecked(true);
                        textOccupied.setText(getString(R.string.occupied));
                        textOccupied.setTextColor(ContextCompat.getColor(HideDetailActivity.this, R.color.redText));
                        setOccupied(true);
                    }

                }
            });
        }

        Button buttonMap = (Button) findViewById(R.id.buttonShowMap);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HideDetailActivity.this, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("HIDE", hide);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Button buttonAddComment = (Button) findViewById(R.id.buttonAddComment);
        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Přidejte komentář");
                DialogHelper.createCommentDialog(HideDetailActivity.this);
            }
        });

        recyclerView = ((RecyclerView) findViewById(R.id.recyclerView));
        setupAdapters(hide.getComments());
    }

    private void setOccupied(boolean isOccupied){
        hide.setOccupied(isOccupied);
        RestClient.get().setHideOccupied(hide.getId(), Preferences.getUserName(), isOccupied).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //pokud by se nám vrátilo
                if (response.code() == 200) {
                    //máme posed obsazený/nebo jsme ho odblokovali - a osatní uživatelé to uvidí
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void setupAdapters(List<Comment> list) {
        commentAdapter = new CommentAdapter(list);
        recyclerView.setAdapter(commentAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void refreshList(Comment newComment) {

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String commentDate = df.format(today);
        newComment.setDate(commentDate);

        newComment.setUser(Preferences.getUserName());

        commentAdapter.addNewItemToTop(newComment);
        //TODO zde bychom provolali metodu RestClient.addComment
        /**
         * Ale protože naše api nejede :( musíme si vystačit pouze s takovou runtime funkčností
         *
         */
    }

    //adaptér k recycler view
    class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

        private List<Comment> items;

        public CommentAdapter(List<Comment> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            return new CommentAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
            holder.hideTitleTextView.setText(items.get(position).getComment());
            holder.hideSubTitleTextView.setText(items.get(position).getDate());
            holder.userCom.setText(items.get(position).getUser());

        }

        @Override
        public int getItemCount() {
            return items == null ? 0 : items.size();
        }

        public void addNewItemToTop(Comment comment) {
            items.add(0, comment);
            notifyItemInserted(0);
            linearLayoutManager.scrollToPositionWithOffset(0, 0);
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView hideTitleTextView;
            TextView hideSubTitleTextView;
            TextView userCom;

            public ViewHolder(View itemView) {
                super(itemView);

                hideTitleTextView = ((TextView) itemView.findViewById(R.id.hideTitleTextView));
                hideSubTitleTextView = ((TextView) itemView.findViewById(R.id.hideSubTitleTextView));
                userCom = (TextView) itemView.findViewById(R.id.userCom);

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }


            @Override
            public void onClick(View v) {
                }

            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        }
    }
}
