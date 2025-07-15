package com.example.projectprmexe.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprmexe.R;
import com.example.projectprmexe.models.UserListResponse;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private List<UserListResponse.UserItem> users;
    private OnUserActionListener listener;

    public interface OnUserActionListener {
        void onDeleteUser(int userId);
        void onChangeRole(int userId, String currentRole);
        void onViewUser(int userId);
    }

    public UserListAdapter(List<UserListResponse.UserItem> users, OnUserActionListener listener) {
        this.users = users;
        this.listener = listener;
    }

    public void updateUsers(List<UserListResponse.UserItem> newUsers) {
        this.users = newUsers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserListResponse.UserItem user = users.get(position);
        holder.bind(user, listener);
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserName, tvUserEmail, tvUserRole, tvUserStatus;
        private Button btnView, btnChangeRole, btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);
            tvUserStatus = itemView.findViewById(R.id.tvUserStatus);
            btnView = itemView.findViewById(R.id.btnView);
            btnChangeRole = itemView.findViewById(R.id.btnChangeRole);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(UserListResponse.UserItem user, OnUserActionListener listener) {
            tvUserName.setText(user.getFullName());
            tvUserEmail.setText(user.getEmail());
            tvUserRole.setText(user.getRoleName());
            tvUserStatus.setText(user.isEmailConfirmed() ? "Verified" : "Unverified");

            // Hide delete button for admin users
            if (user.getRoleId() == 4) {
                btnDelete.setVisibility(View.GONE);
                btnChangeRole.setVisibility(View.GONE);
            } else {
                btnDelete.setVisibility(View.VISIBLE);
                btnChangeRole.setVisibility(View.VISIBLE);
            }

            btnView.setOnClickListener(v -> listener.onViewUser(user.getUserId()));
            btnChangeRole.setOnClickListener(v -> listener.onChangeRole(user.getUserId(), user.getRoleName()));
            btnDelete.setOnClickListener(v -> listener.onDeleteUser(user.getUserId()));
        }
    }
}
