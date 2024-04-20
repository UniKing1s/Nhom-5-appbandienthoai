import mongoose from "mongoose";
const schema = new mongoose.Schema(
  {
    maHoaDon: {
      type: Number,
      required: true,
    },
    username: {
      type: String,
      required: true,
    },
    tenNguoiNhan: {
      type: String,
      required: true,
    },
    phoneNumber: {
      type: String,
      required: true,
    },
    diaChi: {
      type: String,
      required: true,
    },
    tongTien: {
      type: Number,
      required: true,
    },
    loaiThanhToan: {
      type: String,
      required: true,
    },
    ngayHoaDon: {
      type: String,
      // default: new Date(),
      required: false,
    },
    tinhtrang: {
      type: Boolean,
      default: 0,
      required: false,
    },
    chiTietHoaDon: {
      type: Object,
      required: true,
    },
  },
  { versionKey: false }
);
export const billModel = mongoose.model("bill", schema);
