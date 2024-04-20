import mongoose from "mongoose";
const schema = new mongoose.Schema(
  {
    username: {
      type: String,
      required: true,
    },
    masp: {
      type: Number,
      required: true,
    },
    name: {
      type: String,
      required: true,
    },
    quantity: {
      type: Number,
      required: true,
    },
    price: {
      type: Number,
      required: true,
    },
    sale: {
      type: Number,
      required: true,
    },
    totalPrice: {
      type: Number,
      required: true,
    },
    img: {
      type: String,
      required: true,
    },
  },
  { versionKey: false }
);
export const cartUserModel = mongoose.model("cartUser", schema);
