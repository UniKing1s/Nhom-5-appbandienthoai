import express from "express";
// import {
//   createProduct,
//   deleteProduct,
//   getProduct,
//   updateProduct,
// } from "../controllers/products.js";
import {
  createAccount,
  deleteAccount,
  getAccount,
  getAccountToLogin,
  getAccountByUser,
  updateAccount,
} from "../controllers/accounts.js";
const router = express.Router();

//get account
router.get("/", getAccount);
//get account to login
router.post("/login/", getAccountToLogin);
//get account by user
router.get("/getByUsser/:username", getAccountByUser);
//create account
router.post("/", createAccount);
// router.delete("/product", deleteProduct);
//delete account
router.delete("/", deleteAccount);
//update account
router.post("/update", updateAccount);
export default router;
