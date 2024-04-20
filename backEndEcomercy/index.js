import express, { urlencoded } from "express";
import bodyParser from "body-parser";
import cors from "cors";
import products from "./router/products.js";
import accounts from "./router/accounts.js";
import bills from "./router/bills.js";
import brands from "./router/brands.js";
import cartUsers from "./router/cartUsers.js";
import mongoose from "mongoose";
import "dotenv/config.js";
import multer from "multer";
import path from "path";
import { mkdirp } from "mkdirp";
import fs from "fs";
const app = express();
// const url = `${process.env.MongoDB_url}`;
app.use(bodyParser.json({ limit: "30mb" }));
app.use(bodyParser.urlencoded({ extends: true, limit: "30mb" }));
// app.use(
//   cors({
//     origin: ["https://web-shop-nhom5-git-main-uniking1s.vercel.app"],
//     methods: ["POST", "PUT", "GET", "DELETE"],
//     credentials: true,
//   })
// );

// app.use(
//   cors({
//     origin: ["http://localhost:3000"],
//     methods: ["POST", "PUT", "GET", "DELETE", "PATCH"],
//     credentials: true,
//   })
// );

app.use(
  cors({
    origin: "*",
    methods: ["POST", "PUT", "GET", "DELETE", "PATCH"],
    credentials: true,
  })
);
// use to show image
app.use(express.static("public"));
////sử dụng multer để sử lí upload file
// Tự tạo thư mục upload nếu chưa tồn tại
const uploadDir = path.join(process.cwd(), "public/images");
mkdirp.sync(uploadDir);
// Thực hiện thêm ảnh từ request gửi tới
const storage = multer.diskStorage({
  destination: uploadDir,
  filename: (req, file, cb) => {
    cb(
      null,
      new Date().getFullYear() +
        (new Date().getMonth() + 1 < 10
          ? "0" + (new Date().getMonth() + 1)
          : new Date().getMonth() + 1) +
        "" +
        (new Date().getUTCDate() < 10
          ? "0" + new Date().getUTCDate()
          : new Date().getUTCDate()) +
        "" +
        (new Date().getHours() < 10
          ? "0" + new Date().getHours()
          : new Date().getHours()) +
        "" +
        (new Date().getMinutes() < 10
          ? "0" + new Date().getMinutes()
          : new Date().getMinutes()) +
        "" +
        (new Date().getSeconds() < 10
          ? "0" + new Date().getSeconds()
          : new Date().getSeconds()) +
        "" +
        new Date().getMilliseconds() +
        file.originalname
    );
  },
});
app.use(multer({ storage }).single("image"));

// , {
//     useNewUrlParser: true,
//     useUnifiedTopology: true,
//   }
mongoose
  .connect(`${process.env.MongoDB_url}`)
  .then(() => {
    console.log("connected to db");
    app.listen(5000, () => console.log(`server is runing`));
  })
  .catch(() => {
    console.log("connected fail");
  });

//xóa file trong thưu mục
// Xóa file

app.post("/deleteImg/", async (req, res) => {
  try {
    const fileName = req.body.fileName;
    console.log("fileNAme");
    console.log(fileName);
    fs.unlink("public/images/" + fileName, (err) => {
      if (err) {
        res.status(500).json({ err: err });
      } else {
        res.status(200).json({ info: "Xóa thành công file " + fileName });
      }
    });
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
});
app.use("/products", products);
app.use("/accounts", accounts);
app.use("/bills", bills);
app.use("/brands", brands);
app.use("/carts", cartUsers);
// app.use("/product", products);
// app.use("/product", products);
