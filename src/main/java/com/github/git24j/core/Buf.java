package com.github.git24j.core;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class Buf {
    /*
    if jni.NewStringUTF() just simple make a pointer point to char*, I think this will work well, else idk.

    actually this is java String from libgit2's buf->ptr, so this is not identical libgit2's buf->ptr,
     and idk this is a good c string copy or not,
     the source codes is simple use jni.NewStringUTF() get java string from c string,
     the point is "no length of c string when use jni.NewStringUTF()",
     so idk if no '\0' of end, this will work as expected or not.

     relate c code:
     j_mappers.c `void j_git_buf_to_java(JNIEnv *env, git_buf *c_buf, jobject buf)`
     j_mappers.c `void j_call_setter_string_c(JNIEnv *env, jclass clz, jobject obj, const char *method, const char *val)`

     */
    private String ptr;

    private int reserved;
    private int size;  // length of c char ( java byte) not java char

    /** Get internal buffer, generally only the substr up to size is meaningful. */
    String getPtr() {
        return ptr;
    }

    public void setPtr(String ptr) {
        this.ptr = ptr;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Optional<String> getString() {
        if (ptr == null || size == 0) {
            return Optional.empty();  // value of empty Optional is null
        }

        String ret = ptr;

        byte[] src = ptr.getBytes(StandardCharsets.UTF_8);
        //the String maybe over size of c string, so check size, if oversize, cut it
        if(src.length > size) {
            ret = new String(src, 0, size, StandardCharsets.UTF_8);
        }

        return Optional.of(ret);
    }

    @Override
    public String toString() {
        return getString().orElse("");
    }
}
