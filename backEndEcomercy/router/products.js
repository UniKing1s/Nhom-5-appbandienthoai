import express from "express";
import {
  createImage,
  createProduct,
  deleteProduct,
  getProduct,
  getProductById,
  getProductByMaSp,
  updateProduct,
  getSearchProduct,
  updateProductPayed,
  getProductByBrand,
  getProductOnSale,
} from "../controllers/products.js";
// import multer from "multer";
const router = express.Router();

// const storage = multer.diskStorage({
//   destination: function (req, file, cb) {
//     cb(null, "upload/");
//   },
//   filename: function (req, file, cb) {
//     const uniqueSuffix = Date.now();
//     cb(null, uniqueSuffix + file.originalname);
//   },
// });

// const upload = multer({ storage: storage });
//get products
router.get("/", getProduct);
//create products
router.post("/", createProduct);
//get product from search
router.post("/search/", getSearchProduct);
//Upload image
router.post("/uploadImage/", createImage);
//get product byId()
router.put("/byId/", getProductById);
router.get("/byMaSp/:masp", getProductByMaSp);
router.get("/brand/:brand", getProductByBrand);
router.get("/sale", getProductOnSale);
// router.delete("/product", deleteProduct);
//delete product
router.delete("/", deleteProduct);
//update product
router.post("/update/", updateProduct);
//update product quantity in db
router.patch("/updatePayed/", updateProductPayed);
export default router;
