package co.mvvm_dagger_iteo.util

object Constants {

    const val PERSONS = """
            [
                {
                    "_id": "5e5e3d7fc0ea272d00000820",
                    "first_name": "Libby",
                    "last_name": "Predovic",
                    "birth_date": "2014-02-12T00:00:00.000Z",
                    "sex": "M"
                }
            ]
            """

    const val PERSONSUPDATED = """
            [
                {
                    "_id": "5e5e3d7fc0ea272d00000820",
                    "first_name": "Libby",
                    "last_name": "Predovic UPDATED",
                    "birth_date": "2014-02-12T00:00:00.000Z",
                    "sex": "M"
                },
                  {
                    "_id": "1234567890abcdefg12345678",
                    "first_name": "New ",
                    "last_name": "Person",
                    "birth_date": "2011-02-12T00:00:00.000Z",
                    "sex": "F"
                }
            ]
            """

    const val CAR = """
   {
      "_id": "5e5e40c4c0ea272d00000956",
      "brand":"Opel",
      "model":"Astra",
      "color":"#0fc0fc",
      "registration":"WA12345",
      "year":"2005-01-01T00:00:00.000Z",
      "ownerId":"5e5e3d7fc0ea272d00000820",
      "lat":50.754,
      "lng":12.2145
   }
"""
    const val CARS = """
            [
   {
      "_id": "5e5e40c4c0ea272d00000956",
      "brand":"Opel",
      "model":"Astra",
      "color":"#0fc0fc",
      "registration":"WA12345",
      "year":"2005-01-01T00:00:00.000Z",
      "ownerId":"5e5e3d7fc0ea272d00000820",
      "lat":50.754,
      "lng":12.2145
   },

   {
      "_id": "123456abcdf1234567899",
      "brand":"mercedes",
      "model":"benz",
      "color":"#ffffff",
      "registration":"WA678910",
      "year":"2010-01-01T00:00:00.000Z",
      "ownerId":"5e5e3d7fc0ea272d00000820",
      "lat":50.754,
      "lng":12.2145
   }
]
            """

    const val CARSUPDATED = """
            [
   {
       "_id": "5e5e40c4c0ea272d00000956",
      "brand":"Opel",
      "model":"Astra2",
      "color":"#0fc0fc",
      "registration":"WA12345",
      "year":"2005-01-01T00:00:00.000Z",
      "ownerId":"5e5e3d7fc0ea272d00000820",
      "lat":50.754,
      "lng":12.2145
   },

   {
      "_id": "123456abcdf1234567899",
      "brand":"mercedes ",
      "model":"benz UPDATED",
      "color":"#ffffff",
      "registration":"WA678910",
      "year":"2010-01-01T00:00:00.000Z",
      "ownerId":"5e5e3d7fc0ea272d00000820",
      "lat":50.754,
      "lng":12.2145
   },
     {
      "_id": "789919abcdf123456789",
      "brand":"new brand ",
      "model":"new model",
      "color":"#00000",
      "registration":"WA674567",
      "year":"208-01-01T00:00:00.000Z",
      "ownerId":"5e5e3d7fc0ea272d00000820",
      "lat":50.754,
      "lng":12.2145
   }
]
            """

}