package com.vudrag.knjiznica.search;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vudrag.knjiznica.R;
import com.vudrag.knjiznica.dataObjects.Book;

import java.util.List;

public class SearchBooksRecAdapter extends RecyclerView.Adapter<SearchBooksRecAdapter.ViewHolder> {

    private List<Book> books;
    private BookListener bookListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        private TextView title;
        private TextView author;
        private TextView genre;

        BookListener bookListener;

        public ViewHolder(@NonNull View itemView, BookListener bookListener) {
            super(itemView);
            title = itemView.findViewById(R.id.search_books_title);
            author = itemView.findViewById(R.id.search_books_author);
            genre = itemView.findViewById(R.id.search_books_genre);

            itemView.setOnClickListener(this);
            this.bookListener = bookListener;
        }

        @Override
        public void onClick(View view) {
            bookListener.onBookClick(getAdapterPosition());
        }
    }

    public SearchBooksRecAdapter(List<Book> books, BookListener bookListener) {
        this.books = books;
        this.bookListener = bookListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_search_books, parent, false);

        return new ViewHolder(view, bookListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(books.get(position).getName());
        holder.author.setText(books.get(position).getAuthor());
        holder.genre.setText(books.get(position).getGenre());
        if(position % 2 == 0){
            holder.itemView.setBackgroundColor(Color.parseColor("#E6F2Fa"));
        }
        else{
            holder.itemView.setBackgroundColor(Color.parseColor("#F7fBFD"));
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    interface BookListener{
        void onBookClick(int position);
    }
}
