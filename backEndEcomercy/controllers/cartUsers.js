import { cartUserModel } from "../models/cartUserModel.js";
export const createCart = async (req, res) => {
  try {
    const newCart = req.body;
    console.log(newCart);
    for (let i of newCart) {
      const checkCart = await cartUserModel.findOne({
        username: i.username,
        masp: i.masp,
      });
      if (checkCart) {
        updateCart(i);
      } else {
        const cart = new cartUserModel(i);
        await cart.save();
      }
    }
    const carts = await cartUserModel.find({ username: newCart[0].username });
    res.json(carts);
  } catch (err) {
    res.status(500).json({ error: "Tạo giỏ hàng thất bại" });
  }
};
export const addToCart = async (req, res) => {
  try {
    const newCart = req.body;
    console.log(newCart);
    const checkCart = await cartUserModel.findOne({
      username: newCart.username,
      masp: newCart.masp,
    });

    if (checkCart) {
      updateCart(newCart);
      res.json({ status: "done update" });
    } else {
      const cart = new cartUserModel(newCart);
      await cart.save();
      res.json({ status: "done add" });
    }
  } catch (err) {
    res.status(500).json({ error: "Thêm vào giỏ hàng thất bại" });
  }
};
export const updateCartdb = async (req, res) => {
  try {
    const newCart = req.body;
    const checkCart = await cartUserModel.findOne({
      username: newCart.username,
      masp: newCart.masp,
    });
    if (checkCart) {
      await cartUserModel.updateOne(
        {
          username: newCart.username,
          masp: newCart.masp,
        },
        {
          $set: {
            quantity: newCart.quantity,
            totalPrice: newCart.totalPrice,
          },
        }
      );
      res.json({ mess: "success" });
    } else {
      res.status(404).json({ err: "Not found" });
    }
  } catch (err) {
    res.status(500).json({ error: "update giỏ hàng thất bại" });
  }
};
export const getCart = async (req, res) => {
  try {
    const username = req.params.username;
    console.log(username);
    const checkCart = await cartUserModel.find({
      username: username,
    });
    if (checkCart.length > 0 && checkCart !== null) {
      res.json(checkCart);
    } else {
      res.status(404).json({ mess: "No cart found" });
    }
  } catch (err) {
    res.status(500).json({ error: "Lấy giỏ hàng thất bại" });
    // console.log("err");
    // console.log(res.status);
  }
};
export const deleteCart = async (req, res) => {
  try {
    const newCart = req.body;
    const checkCart = await cartUserModel.deleteOne({
      username: newCart.username,
      masp: newCart.masp,
    });
    res.json({ mess: "success" });
  } catch (err) {
    res.status(500).json({ error: "xóa giỏ hàng thất bại" });
  }
};
export const deleteCartAfterPayed = async (req, res) => {
  try {
    const username = req.params.username;
    const checkCart = await cartUserModel.deleteMany({
      username: username,
      quantity: { $gt: 0 },
    });
    res.json({ mess: "success" });
  } catch (err) {
    res.status(500).json({ error: "xóa giỏ hàng thất bại" });
  }
};
const updateCart = async (cart) => {
  await cartUserModel.updateOne(
    {
      username: cart.username,
      masp: cart.masp,
    },
    {
      $inc: {
        quantity: cart.quantity,
        totalPrice: cart.totalPrice,
      },
    }
  );
  return cart;
};
