package com.nudemeth.example.web.controller

import org.scalatest.FunSuiteLike
import org.scalatra.test.scalatest.ScalatraSuite

class DataControllerTests extends ScalatraSuite with FunSuiteLike {
  addServlet(classOf[DataController], "/data/*")

  test("GET /data/home on DataController should return \"This is Home page\" text in body content"){
    get("/data/home"){
      body should include ("This is Home page")
    }
  }

  test("GET /data/about on DataController should return \"About page\" text in body content"){
    get("/data/about"){
      body should include ("About page")
    }
  }
}
