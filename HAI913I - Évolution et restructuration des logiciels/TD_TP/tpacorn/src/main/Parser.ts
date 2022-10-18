import { parse } from "acorn";
import {readFileSync} from "fs";

function parseFile(filePath : string) {
    let inputFile = readFileSync(filePath, "utf-8");
    console.log(parse(inputFile, {ecmaVersion: 2020}));
}
export {parseFile};