package com.nudemeth.example.web.viewmodel

final case class HomeViewModel(greeting: String, override val title: String) extends ViewModel(title) {
  def this(text: String) = this(text, "Home")
}
