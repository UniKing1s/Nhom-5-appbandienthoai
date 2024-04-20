// import multer from "multer";
// import fs from "fs";
import { productModel } from "../models/productModel.js";
// import path from "path";
// import { mkdirp } from "mkdirp";
//get products

export const getProduct = async (req, res) => {
  try {
    const products = await productModel.find();
    res.status(200).json(products);
    console.log("product", products);
    // console.log(res.data.accessToken);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};
export const getProductById = async (req, res) => {
  try {
    const productsObject = req.body;
    console.log(productsObject);
    const products = await productModel.findById(productsObject._id);
    res.json(products);
    console.log("product", products);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};

export const getProductByBrand = async (req, res) => {
  try {
    const brand = req.params.brand;
    console.log(brand);
    const products = await productModel.find({ type: brand });
    res.json(products);
    console.log("products: ", products);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};

export const getProductOnSale = async (req, res) => {
  try {
    // const brand = req.params.brand;
    // console.log(brand);
    const products = await productModel
      .find({
        sale: { $gt: 0 },
        quantity: { $gt: 0 },
      })
      .sort({ sale: -1 });
    res.json(products);
    console.log("products: ", products);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};

export const getProductByMaSp = async (req, res) => {
  try {
    const masp = req.params.masp;
    // console.log(productsObject);
    const products = await productModel.findOne({ masp: masp });
    res.status(200).json(products);
    console.log("product", products);
    // console.log(req.body);
    // const book = await sachModel.findOne({ maSach: bookObject.maSach });
    // res.status(200).json(book);

    // console.log(res.data.accessToken);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};

export const createProduct = async (req, res) => {
  try {
    const newProduct = req.body;
    console.log("body");
    console.log(req.body);
    console.log("file");
    console.log(req.files);
    const maxMaSp = await productModel.find().sort({ masp: -1 }).limit(1);
    if (!maxMaSp[0] || !maxMaSp || maxMaSp.length == 0) {
      console.log("k có max mã");
      newProduct["masp"] = 1;
      console.log(maxMaSp[0]);
    } else {
      console.log("có max mã");
      console.log(maxMaSp[0]);
      newProduct["masp"] = Number(maxMaSp[0].masp) + 1;
    }
    // newProduct["img"] = filename;
    console.log(newProduct);
    const product = new productModel(newProduct);
    await product.save();
    res.status(200).json(product);
    console.log("product controller", product);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err: ", err);
  }
};
export const createImage = async (req, res) => {
  try {
    if (!req.file) {
      res.status(400).send({ error: "No file uploaded" });
      console.log("no file to upload");
      return;
    }
    console.log(req.file);
    // console.log("1");
    // Lưu file ảnh vào thư mục upload
    const filename = req.file.filename;
    console.log(req.file.filename);

    // Trả về thông báo thành công
    res.status(200).json({
      success: true,
      filename: filename,
    });
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};
export const deleteProduct = async (req, res) => {
  try {
    const deleteProduct = req.body;
    await productModel.deleteOne({ masp: deleteProduct.masp });
    // await product.save();
    res.status(200).json({ delete: "success" });
    console.log("deleted product");
    // console.log("product", product);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};

//update product with masp
export const updateProduct = async (req, res) => {
  try {
    const updateProduct = req.body;
    const product = await productModel.findOneAndUpdate(
      { masp: updateProduct.masp },
      updateProduct,
      { new: true }
    );
    await product.save();
    res.status(200).json({ product });
    // console.log("product", product);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};
export const getSearchProduct = async (req, res) => {
  try {
    const productsObject = req.body;
    console.log(productsObject);
    const products = await productModel.find({
      name: { $regex: productsObject.name, $options: "i" },
    });
    // { name: { $regex: 'TP Hồ Chí Minh', $options: 'i' } }
    res.status(200).json(products);
    console.log("product", products);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};
// ///controller Api xử lí thanh toán
// export const directPaying = async (req, res) => {
//   try {
//     const products = req.body;
//     // console.log(productsObject);
//     // const products = await productModel.find({
//     //   name: { $regex: productsObject.name, $options: "i" },
//     // });
//     // { name: { $regex: 'TP Hồ Chí Minh', $options: 'i' } }
//     res.status(200).json(products);
//     console.log("product", products);
//   } catch (err) {
//     res.status(500).json({ error: err });
//     console.log("err");
//   }
// };

export const updateProductPayed = async (req, res) => {
  try {
    // {
    //   "products":[
    //     {
    //     "masp":1,
    //     "quantity": 20
    //     },
    //     {
    //     "masp":2,
    //     "quantity": 20
    //     }
    //   ]
    // }
    const updateProducts = req.body.products;
    // console.log(updateProducts);
    for (let product of updateProducts) {
      await doUpdateProduct(product.masp, product.quantity);
    }
    // await product.save();
    res.status(200).json({ status: "success" });
    // console.log("product", product);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};
const doUpdateProduct = async (masp, quantity) => {
  await productModel.updateOne(
    { masp: masp },
    { $inc: { quantity: -quantity } }
  );
};
