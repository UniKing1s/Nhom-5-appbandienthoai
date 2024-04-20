import mongoose from "mongoose";
const schema = new mongoose.Schema(
  {
    masp: {
      type: Number,
      required: true,
    },
    name: {
      type: String,
      required: true,
    },
    type: {
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
      required: false,
      default: 0,
    },
    decribtion: {
      type: String,
      required: false,
    },
    status: {
      type: String,
      required: true,
    },
    img: {
      type: String,
      required: true,
    },
    // img: {
    //   data: Buffer,
    //   contentType: String,
    // },
  },
  { versionKey: false }
);
export const productModel = mongoose.model("product", schema);
