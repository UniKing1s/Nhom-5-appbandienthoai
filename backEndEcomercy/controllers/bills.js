import { billModel } from "../models/billModel.js";
import { productModel } from "../models/productModel.js";

export const createBill = async (req, res) => {
  try {
    const newBill = req.body;
    console.log(req.body);
    const maxBill = await billModel.find().sort({ maHoaDon: -1 }).limit(1);
    if (maxBill.length < 1) {
      newBill["maHoaDon"] = 1;
      newBill["ngayHoaDon"] = new Date();
    } else {
      console.log("có max mã");
      console.log(maxBill[0]);
      newBill["maHoaDon"] = Number(maxBill[0].maHoaDon) + 1;
      newBill["ngayHoaDon"] = new Date();
    }
    console.log(newBill);
    const bill = new billModel(newBill);
    await bill.save();
    res.json({ message: "success" });
    console.log("bill", bill);
  } catch (err) {
    res.status(500).json({ error: "Tạo hóa đơn thất bại" });
    // console.log("err");
    // console.log(res.status);
  }
};
export const getAllBill = async (req, res) => {
  try {
    const allBill = await billModel.find({}).sort({ ngayhoadon: -1 });
    if (allBill.length > 0 && allBill !== null) {
      res.json(allBill);
    } else {
      res.status(404).json({ error: "Không tồn tại bất kì hóa đơn nào" });
    }
  } catch (err) {
    res.status(500).json({ error: "Lấy thông tin hóa đơn thất bại" });
  }
};

export const getBillByUserAndMaHoaDon = async (req, res) => {
  try {
    const username = req.query.username;
    const maHoaDon = req.query.maHoaDon;
    console.log(username + "and" + maHoaDon);
    const bill = await billModel.findOne({
      username: username,
      maHoaDon: maHoaDon,
    });
    if (bill) {
      res.json(bill);
    } else {
      res.status(404).json({ error: "Không tồn tại hóa đơn này" });
    }
  } catch (err) {
    res.status(500).json({ error: "Lấy thông tin hóa đơn thất bại" });
  }
};

export const getBillByUser = async (req, res) => {
  try {
    const username = req.params.username;
    console.log(username);
    const bills = await billModel.find({
      username: username,
    });
    if (bills.length > 0) {
      res.json(bills);
    } else {
      res.status(404).json({ error: "Không tồn tại bất kì hóa đơn nà0" });
    }
  } catch (err) {
    res.status(500).json({ error: "Lấy thông tin hóa đơn thất bại" });
  }
};

export const deleteBill = async (req, res) => {
  try {
    const deleteBill = req.body;
    await _updateBillHoanTra(deleteBill.chiTietHoaDon);
    await billModel.deleteOne({
      maHoaDon: deleteBill.maHoaDon,
      username: deleteBill.username,
    });
    res.status(200).json({ deleteBill: "success" });
    console.log("deleted bill");
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};

export const updateBillPayed = async (req, res) => {
  try {
    const billPayed = req.body;
    await billModel.updateOne(
      {
        maHoaDon: billPayed.maHoaDon,
        username: billPayed.username,
      },
      { $set: { tinhtrang: true } }
    );
    res.status(200).json({ updateTinhTrang: "success" });
    console.log("update bill");
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};

const _updateBillHoanTra = async (products) => {
  for (let product of products) {
    await productModel.updateOne(
      { masp: product.masp },
      { $inc: { quantity: product.quantity } }
    );
  }
};
