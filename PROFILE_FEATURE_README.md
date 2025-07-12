# Tính năng Thông tin cá nhân (User Profile)

## Tổng quan
Đã thêm tính năng quản lý thông tin cá nhân cho ứng dụng Android. Người dùng có thể xem và cập nhật thông tin cá nhân của mình.

## Các file đã thêm/sửa đổi

### 1. Models
- `UserProfile.java` - Model cho thông tin user
- `UpdateProfileRequest.java` - Model cho request cập nhật profile

### 2. API Interface
- `AuthApi.java` - Thêm endpoints:
  - `GET /api/Auth/profile` - Lấy thông tin profile
  - `PUT /api/Auth/profile` - Cập nhật thông tin profile

### 3. Activity
- `ProfileActivity.java` - Activity chính cho quản lý profile
- Layout: `activity_profile.xml`

### 4. Menu
- `menu_main.xml` - Menu với option "Thông tin cá nhân"

### 5. Cập nhật các Activity khác
- `MainActivity.java` - Thêm nút "Thông tin cá nhân"
- `ProductListActivity.java` - Thêm menu option
- `CartActivity.java` - Thêm menu option

## Cách sử dụng

### 1. Truy cập từ MainActivity
- Mở ứng dụng và đăng nhập
- Trên màn hình chính, nhấn nút "Thông tin cá nhân"

### 2. Truy cập từ menu
- Trong ProductListActivity hoặc CartActivity
- Nhấn vào icon menu (3 chấm) trên toolbar
- Chọn "Thông tin cá nhân"

### 3. Chức năng trong ProfileActivity
- **Xem thông tin**: Tự động load thông tin user từ server
- **Chỉnh sửa thông tin**:
  - Họ và tên
  - Email (chỉ xem, không thể thay đổi)
  - Số điện thoại
  - Địa chỉ
  - Giới tính (Nam/Nữ)
  - Ngày sinh (DatePicker)
- **Đổi mật khẩu (tùy chọn)**:
  - Mật khẩu cũ
  - Mật khẩu mới
  - Xác nhận mật khẩu mới
- **Lưu thay đổi**: Nhấn "Lưu thay đổi" để cập nhật
- **Hủy**: Nhấn "Hủy" hoặc nút back để quay lại

## Validation
- **Thông tin cá nhân**: Tất cả các trường đều bắt buộc
- **Đổi mật khẩu (tùy chọn)**:
  - Nếu muốn đổi mật khẩu, phải nhập đầy đủ 3 trường
  - Mật khẩu mới phải có ít nhất 6 ký tự
  - Mật khẩu xác nhận phải khớp với mật khẩu mới
- Ngày sinh được chọn qua DatePicker

## API Endpoints
```
GET /api/Auth/profile
Headers: Authorization: Bearer {token}
Response: UserProfile

PUT /api/Auth/update-profile
Headers: Authorization: Bearer {token}
Body: UpdateProfileRequest
Response: String (success message)
```

## Giao diện
- Sử dụng Material Design components
- Responsive layout với ScrollView
- DatePicker cho ngày sinh
- Spinner cho giới tính
- Validation với error messages

## Lưu ý
- Cần đăng nhập để sử dụng tính năng
- Token được lưu trong SharedPreferences
- Nếu token hết hạn, sẽ chuyển về LoginActivity
- Tất cả API calls đều có error handling 