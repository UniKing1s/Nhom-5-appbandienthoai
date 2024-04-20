import express from "express";
// import {
//   createProduct,
//   deleteProduct,
//   getProduct,
//   updateProduct,
// } from "../controllers/products.js";
import {
  createBrand,
  deleteBrand,
  getBrandById,
  getBrand,
  updateBrand,
} from "../controllers/brands.js";
const router = express.Router();

//get account
router.get("/", getBrand);
//get account by user
router.get("/:id", getBrandById);
//create account
router.post("/", createBrand);
// router.delete("/product", deleteProduct);
//delete account
router.delete("/:id", deleteBrand);
//update account
router.patch("/", updateBrand);
export default router;
