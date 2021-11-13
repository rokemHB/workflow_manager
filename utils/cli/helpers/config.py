import configparser
import logging
import os


class Config:
    """
    Class to handle config access
    """

    _config = None

    def __init__(self, config_path):
        """
        Init Config
        :param config_path: Path to the configuration file this Config object is handling
        """
        if os.path.isfile(config_path):
            self._load_config(config_path)
        else:
            self._create_default_config(config_path)

    def _load_config(self, config_path):
        """
        Loads the file located at config_path.
        :param config_path: Path of configuration file to be loaded
        """
        logging.info(f"Loading config file {config_path}")
        config = configparser.ConfigParser()
        config.read(config_path)
        self._config = config

    def _create_default_config(self, config_path):
        """
        Creates default config at config_path.
        :param config_path: Path of configuration file to be created.
        """
        logging.info(f"Creating config file {config_path}")
        config = configparser.ConfigParser()
        config['DEFAULT'] = {
            "base_url": "http://localhost:8080/workflow-manager-0.1-SNAPSHOT",
        }
        config['CREDENTIALS'] = {
            "username": "<YOUR_USERNAME>",
            "password": "<YOUR_PASSWORD>"
        }
        with open(config_path, "w") as configfile:
            config.write(configfile)
        self._config = config

    def get_value(self, key, section="DEFAULT"):
        return self._config[section].get(key)
