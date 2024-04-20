import { accountModel } from "../models/accountModel.js";
// import { productModel } from "../models/productModel.js";

//get products
export const getAccount = async (req, res) => {
  try {
    const accounts = await accountModel.find();
    res.status(200).json(accounts);
    console.log("account", accounts);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};
export const getAccountByUser = async (req, res) => {
  try {
    const username = req.params.username;
    // console.log(username);
    const account = await accountModel.findOne({ username: username });
    if (account) {
      res.status(200).json({ account });
    } else {
      res.status(500).json({ err: "không có dữ liệu" });
    }
    console.log("account", account);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};
export const getAccountToLogin = async (req, res) => {
  try {
    const accountToLogin = req.body;
    const accounts = await accountModel.findOne({
      username: accountToLogin.username,
      password: accountToLogin.password,
    });
    if (accounts !== null) {
      res.status(200).json(accounts);
      console.log("account", accounts);
    } else {
      res.status(500).json(accounts);
    }
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};

//create product
export const createAccount = async (req, res) => {
  try {
    const newAccount = req.body;
    console.log(req.body);
    // const findAccount = await accountModel.countDocuments({
    //   username: newAccount.username,
    // });
    if (
      (await accountModel.countDocuments({ username: newAccount.username })) ===
      0
    ) {
      const account = new accountModel(newAccount);
      await account.save();
      res.status(200).json(account);
      console.log("account", account);
    } else {
      res.status(500).json({ error: "Tài khoản đã tồn tại" });
      console.log("err");
    }
  } catch (err) {
    res.status(500).json({ error: "Tài khoản đã tồn tại" });
    // console.log("err");
    // console.log(res.status);
  }
};
export const deleteAccount = async (req, res) => {
  try {
    const deleteAccount = req.body;
    await accountModel.deleteOne({
      username: deleteAccount.username,
      password: deleteAccount.password,
    });
    // await product.save();

    res.status(200).json({ deleteAccount: "success" });
    console.log("deleted Account");
    // console.log("product", product);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};

//update product with masp
export const updateAccount = async (req, res) => {
  try {
    const updateAccount = req.body;
    const account = await accountModel.findOneAndUpdate(
      { username: updateAccount.username },
      updateAccount,
      { new: true }
    );
    await account.save();
    res.status(200).json({ account });
    // console.log("product", product);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};
