import express from "express";
import {
  addToCart,
  createCart,
  deleteCart,
  deleteCartAfterPayed,
  getCart,
  updateCartdb,
} from "../controllers/cartUsers.js";

const router = express.Router();

// router.get("/", getAccount);
//create Bill
// các giá trị khác ngoại trừ mã hóa đơn

router.post("/", createCart);
router.get("/:username", getCart);
router.post("/updateCart/", updateCartdb);
router.post("/delete", deleteCart);
router.delete("/afterPayed/:username", deleteCartAfterPayed);
router.post("/addToCart", addToCart);
export default router;
