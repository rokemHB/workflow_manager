import argparse
import os

_file = os.path.realpath(__file__)
_file_dir = os.path.abspath(os.path.join(_file, os.pardir))
DEFAULT_CONFIG_PATH = os.path.abspath(os.path.join(_file_dir, os.pardir))
DEFAULT_CONFIG_FILE = os.path.abspath(os.path.join(DEFAULT_CONFIG_PATH, "config.ini"))


def parse_args():
    parser = argparse.ArgumentParser(description="Command Line Interface for Workflow Manager.")
    parser.add_argument('-c', '--config-file', dest='config_file', default=DEFAULT_CONFIG_FILE,
                        help='Change path to config file.')
    parser.add_argument('-l', '--list', dest='list', nargs="?", default=False, const=True, help='List entities.')
    parser.add_argument('-e', '--eval', dest='eval', nargs="?", default=False, const=True, help='Evaluate given class.')
    parser.add_argument('-u', '--update', dest='update', nargs="?", default=False, const=True, help='Update given '
                                                                                                    'class.')
    parser.add_argument('-a', '--add', dest='add', nargs="?", default=False, const=True, help='Add new entity.')
    parser.add_argument('--generate-config', dest='generate_config', default=False, const=True, nargs="?",
                        help="Only generate the requires config file and exit after.")
    return parser.parse_args()
