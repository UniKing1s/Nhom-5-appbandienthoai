

# dowload chương trình và chạy bằng android studio
# rest api tới backend đã deploy sẵn nên không cẩn thay đổi
# source backend có thể chạy lên bằng nodejs với lệnh "npm i" (để tải các module về) và "npm start" để khởi chạy
trên port 5000



# Backend cho các api đã được deploy 
		https://backendphonestore.onrender.com/products
# tài khoản admin dùng cho trang https://khoivo.id.vn/ để quản lý
- wow112
- khoivo99122
# tài khoản user sẵn có hoặc có thể đăng ký trên app
- test12
- test12
# quản lý các sản phẩm đang bán và thêm sửa thông tin sản phẩm, hóa đơn người dùng, tài khoản người dùng tại trang: 		https://khoivo.id.vn/
(Đăng nhập với tài khoản admin)

# các class trong app có chứa api cần thay đổi nếu thay đổi restapi tới backend trên máy
- CartAdapter
- line 131 - 133 CartAdapter (https://backendphonestore.onrender.com/images/" + tên hình ảnh)
- RegisterActivity
- LoginActivity
- HomeFragment
- HomeRecycleViewAdapter
- ProductDetaileActivity

# Backend chạy trên port 5000

