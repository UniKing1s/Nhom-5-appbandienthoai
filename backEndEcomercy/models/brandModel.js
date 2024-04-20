import mongoose from "mongoose";
const schema = new mongoose.Schema(
  {
    id: {
      type: String,
      required: true,
    },
    name: {
      type: String,
      required: true,
    },
  },
  { versionKey: false }
);
export const brandModel = mongoose.model("brand", schema);
