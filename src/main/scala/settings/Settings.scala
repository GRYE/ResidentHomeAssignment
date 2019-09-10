package settings

final case class Settings()

final case class KinesisSettings(region: String,
                                 accessKey: String,
                                 secretAcessKey: String,
                                 streamName: String)
