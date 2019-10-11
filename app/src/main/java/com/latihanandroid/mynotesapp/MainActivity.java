package com.latihanandroid.mynotesapp;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.latihanandroid.mynotesapp.adapter.NoteAdapter;
import com.latihanandroid.mynotesapp.db.DatabaseContract;
import com.latihanandroid.mynotesapp.db.NoteHelper;
import com.latihanandroid.mynotesapp.entity.Note;
import com.latihanandroid.mynotesapp.helper.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
LoadNotesCallback{
    private RecyclerView rvNotes;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    private static final String EXTRA_STATE="EXTRA_STATE";
    private NoteAdapter adapter;
    private NoteHelper noteHelper;

    private static HandlerThread handlerThread;
    private DataObserver myObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Notes");
        }

        rvNotes=findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);

        noteHelper=NoteHelper.getInstance(getApplicationContext());
        noteHelper.open();

        progressBar=findViewById(R.id.progressBar);

        handlerThread=new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler= new Handler(handlerThread.getLooper());
        myObserver=new DataObserver(handler,this);
        getContentResolver().registerContentObserver(DatabaseContract.NoteColumns.CONTENT_URI,true,myObserver);
        fabAdd=findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(this);

        adapter=new NoteAdapter(this);
        rvNotes.setAdapter(adapter);

        if (savedInstanceState==null){
            new LoadNotesAsync(this,this).execute();
        }else {
            ArrayList<Note> list=savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list!=null){
                adapter.setListNote(list);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE,adapter.getListNote());
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.fab_add){
            Intent intent=new Intent(MainActivity.this, NoteAddUpdateActivity.class);
            startActivityForResult(intent,NoteAddUpdateActivity.REQUEST_ADD);
        }
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(Cursor notes) {
        progressBar.setVisibility(View.INVISIBLE);
        ArrayList<Note> listNotes= MappingHelper.mapCursorToArrayList(notes);
        if (listNotes.size()>0){
            adapter.setListNote(listNotes);
        }else {
            adapter.setListNote(new ArrayList<Note>());
            showSnackBarMessage("Tidak ada data saat ini");
        }
    }

    private static class LoadNotesAsync extends AsyncTask<Void,Void,Cursor>{
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadNotesCallback> weakCallback;


        public LoadNotesAsync(Context context, LoadNotesCallback callback) {
            this.weakContext=new WeakReference<>(context);
            this.weakCallback=new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context=weakContext.get();
            Cursor c= context.getContentResolver().query(DatabaseContract.NoteColumns.CONTENT_URI,null,null
                    ,null,null);
            if (c==null){
                Log.d("Eror Check", "doInBackground: ");
            }
            return c;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            if (requestCode==NoteAddUpdateActivity.REQUEST_ADD){
                if (resultCode==NoteAddUpdateActivity.RESULT_ADD){
                    Note note=data.getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE);
//                    adapter.addItem(note);
//                    rvNotes.smoothScrollToPosition(adapter.getItemCount());
                    showSnackBarMessage("Satu item berhasil ditambahkan");
                }
            }else if (requestCode==NoteAddUpdateActivity.REQUEST_UPDATE){
                if (resultCode==NoteAddUpdateActivity.RESULT_UPDATE){
                    Note note=data.getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE);
                    int position=data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION,0);
//                    adapter.updateItem(position,note);
//                    rvNotes.smoothScrollToPosition(position);
                    showSnackBarMessage("Satu item berhasil diubah");
                }else if (resultCode==NoteAddUpdateActivity.RESULT_DELETE){
                    int position=data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION,0);
//                    adapter.removeItem(position);
                    showSnackBarMessage("Satu item berhasil dihapus");
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteHelper.close();
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(rvNotes,message,Snackbar.LENGTH_SHORT).show();
    }

    public static class DataObserver extends ContentObserver {
        final Context context;
        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context=context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadNotesAsync(context, (LoadNotesCallback) context).execute();
        }
    }
}
