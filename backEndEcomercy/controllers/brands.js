import { brandModel } from "../models/brandModel.js";

export const getBrand = async (req, res) => {
  try {
    const brands = await brandModel.find();
    res.status(200).json(brands);
    console.log("brands", brands);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};
export const getBrandById = async (req, res) => {
  try {
    const id = req.params.id;
    const brand = await brandModel.findOne({ id: id });
    if (brand) {
      res.status(200).json(brand);
    } else {
      res.status(500).json({ err: "không có dữ liệu" });
    }
    console.log("brand", brand);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};

//create product
export const createBrand = async (req, res) => {
  try {
    const newBrand = req.body;
    console.log(req.body);
    // const findAccount = await accountModel.countDocuments({
    //   username: newAccount.username,
    // });
    if ((await brandModel.countDocuments({ id: newBrand.id })) === 0) {
      const brand = new brandModel(newBrand);
      await brand.save();
      res.status(200).json(brand);
      console.log("brand: ", brand);
    } else {
      res.status(500).json({ error: "Mã hãng đã tồn tại" });
      console.log("err");
    }
  } catch (err) {
    res.status(500).json({ error: "Mã hãng đã tồn tại" });
    // console.log("err");
    // console.log(res.status);
  }
};
export const deleteBrand = async (req, res) => {
  try {
    const id = req.params.id;
    await brandModel.deleteOne({
      id: id,
    });
    // await product.save();

    res.status(200).json({ deleteBrand: "success" });
    console.log("deleted brand");
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};

//update product with masp
export const updateBrand = async (req, res) => {
  try {
    const updateBrand = req.body;
    const brand = await accountModel.findOneAndUpdate(
      { id: updateBrand.id },
      updateBrand,
      { new: true }
    );
    await brand.save();
    res.status(200).json({ brand });
    // console.log("product", product);
  } catch (err) {
    res.status(500).json({ error: err });
    console.log("err");
  }
};
