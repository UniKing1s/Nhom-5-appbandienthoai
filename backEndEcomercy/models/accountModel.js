import mongoose from "mongoose";
const schema = new mongoose.Schema(
  {
    username: {
      type: String,
      required: true,
    },
    password: {
      type: String,
      required: true,
    },
    // role: {
    //   type: Number,
    //   required: true,
    //   default: 1,
    // },
  },
  { versionKey: false }
);
export const accountModel = mongoose.model("account", schema);
