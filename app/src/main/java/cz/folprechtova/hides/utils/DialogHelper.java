package cz.folprechtova.hides.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

import cz.folprechtova.hides.activity.HideDetailActivity;
import cz.folprechtova.hides.dto.Comment;

public class DialogHelper {

    public static void createCommentDialog(final HideDetailActivity hideActivity) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(hideActivity);
        builder.setTitle("Zadejte komentář");

        final EditText input = new EditText(hideActivity);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        builder.setView(input);

        builder.setPositiveButton("Přidat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!input.getText().toString().isEmpty()) {

                    hideActivity.refreshList(new Comment(input.getText().toString()));
                    dialog.cancel();
                }
            }
        });

        builder.setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
