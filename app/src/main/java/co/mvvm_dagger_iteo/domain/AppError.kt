package co.mvvm_dagger_iteo.domain

data class AppError(var errorMessage:String,
                    var errorTitle:String? = null,
                    var errorTitleResource:Int = -1,
                    var errorMessageResource:Int = -1,
                    var errorCode:Int = -1)