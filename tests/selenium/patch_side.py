import os

FALLBACK_URL = 'http://localhost:8080/workflow-manager-0.1-SNAPSHOT'


def get_side_files(path):
    side_files = [f for f in os.listdir(path)
                  if os.path.isfile(os.path.join(path, f))
                  and f.endswith(".side")
                  and not f.endswith(".out.side")]
    return side_files


def clean_up(path):
    for f in os.listdir(path):
        if f.endswith(".out.side"):
            file_path = os.path.join(path, f)
            print("Removing " + file_path)
            os.remove(file_path)


def patch(line, url):
    if url == FALLBACK_URL or url == FALLBACK_URL:
        return line
    else:
        return line.replace(FALLBACK_URL, url)


def patch_file(f, path=os.path.realpath(__file__)):
    with open(os.path.join(path, f), "r") as input_file:
        output_name = f.strip(".side") + ".out.side"
        url = os.environ.get('KCB_BASE_URL') or FALLBACK_URL
        with open(os.path.join(path, output_name), "w") as output_file:
            for line in input_file:
                output_file.write(patch(line, url))
    return


def main():
    dir_path = os.path.dirname(os.path.realpath(__file__))
    clean_up(dir_path)
    print("Patching side files in: " + dir_path)
    files = get_side_files(dir_path)
    for f in files:
        patch_file(f, path=dir_path)


if __name__ == "__main__":
    main()
