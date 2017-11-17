package com.nudemeth.example.web.controller

import com.nudemeth.example.web.viewmodel._
import org.json4s.jackson.Serialization.write

class DataController extends BaseController {

  get("/home") {
    contentType = "application/json"
    write(HomeViewModel(s"This is Home page"))

  }

  get("/about") {
    contentType = "application/json"
    write(AboutViewModel(s"About page"))
  }

}
