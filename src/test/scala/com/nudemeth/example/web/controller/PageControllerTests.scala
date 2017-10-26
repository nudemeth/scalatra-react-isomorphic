package com.nudemeth.example.web.controller

import org.scalatest.FunSuiteLike
import org.scalatra.test.scalatest._

class PageControllerTests extends ScalatraSuite with FunSuiteLike {

  addServlet(classOf[PageController], "/*")

  test("GET / on PageController should return status 200"){
    get("/"){
      status should equal (200)
    }
  }

  test("GET / on PageController should show \"Hello World\" in body content"){
    get("/"){
      body should include ("Hello World")
    }
  }

}
