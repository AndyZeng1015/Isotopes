package com.isotopes.beans;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class HotSpotsBean implements Parcelable {

    private int showapi_res_code;
    private String showapi_res_error;
    private ShowapiResBodyBean showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public ShowapiResBodyBean getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiResBodyBean showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public static class ShowapiResBodyBean implements Parcelable {
        private int ret_code;
        private PagebeanBean pagebean;

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public PagebeanBean getPagebean() {
            return pagebean;
        }

        public void setPagebean(PagebeanBean pagebean) {
            this.pagebean = pagebean;
        }

        public static class PagebeanBean implements Parcelable {
            private int allPages;
            private int currentPage;
            private int allNum;
            private int maxResult;
            private List<ContentlistBean> contentlist;

            public int getAllPages() {
                return allPages;
            }

            public void setAllPages(int allPages) {
                this.allPages = allPages;
            }

            public int getCurrentPage() {
                return currentPage;
            }

            public void setCurrentPage(int currentPage) {
                this.currentPage = currentPage;
            }

            public int getAllNum() {
                return allNum;
            }

            public void setAllNum(int allNum) {
                this.allNum = allNum;
            }

            public int getMaxResult() {
                return maxResult;
            }

            public void setMaxResult(int maxResult) {
                this.maxResult = maxResult;
            }

            public List<ContentlistBean> getContentlist() {
                return contentlist;
            }

            public void setContentlist(List<ContentlistBean> contentlist) {
                this.contentlist = contentlist;
            }

            public static class ContentlistBean implements Parcelable {
                private String proId;
                private String summary;
                private String cityId;
                private LocationBean location;
                private String cityName;
                private String areaId;
                private String id;
                private String proName;
                private String areaName;
                private String address;
                private String name;
                private String star;
                private String attention;
                private String coupon;
                private String opentime;
                private String content;
                private String price;
                private List<PicListBean> picList;

                public String getProId() {
                    return proId;
                }

                public void setProId(String proId) {
                    this.proId = proId;
                }

                public String getSummary() {
                    return summary;
                }

                public void setSummary(String summary) {
                    this.summary = summary;
                }

                public String getCityId() {
                    return cityId;
                }

                public void setCityId(String cityId) {
                    this.cityId = cityId;
                }

                public LocationBean getLocation() {
                    return location;
                }

                public void setLocation(LocationBean location) {
                    this.location = location;
                }

                public String getCityName() {
                    return cityName;
                }

                public void setCityName(String cityName) {
                    this.cityName = cityName;
                }

                public String getAreaId() {
                    return areaId;
                }

                public void setAreaId(String areaId) {
                    this.areaId = areaId;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getProName() {
                    return proName;
                }

                public void setProName(String proName) {
                    this.proName = proName;
                }

                public String getAreaName() {
                    return areaName;
                }

                public void setAreaName(String areaName) {
                    this.areaName = areaName;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getStar() {
                    return star;
                }

                public void setStar(String star) {
                    this.star = star;
                }

                public String getAttention() {
                    return attention;
                }

                public void setAttention(String attention) {
                    this.attention = attention;
                }

                public String getCoupon() {
                    return coupon;
                }

                public void setCoupon(String coupon) {
                    this.coupon = coupon;
                }

                public String getOpentime() {
                    return opentime;
                }

                public void setOpentime(String opentime) {
                    this.opentime = opentime;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public List<PicListBean> getPicList() {
                    return picList;
                }

                public void setPicList(List<PicListBean> picList) {
                    this.picList = picList;
                }

                public static class LocationBean implements Parcelable {

                    private String lon;
                    private String lat;

                    public String getLon() {
                        return lon;
                    }

                    public void setLon(String lon) {
                        this.lon = lon;
                    }

                    public String getLat() {
                        return lat;
                    }

                    public void setLat(String lat) {
                        this.lat = lat;
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(this.lon);
                        dest.writeString(this.lat);
                    }

                    public LocationBean() {
                    }

                    protected LocationBean(Parcel in) {
                        this.lon = in.readString();
                        this.lat = in.readString();
                    }

                    public static final Creator<LocationBean> CREATOR = new Creator<LocationBean>() {
                        @Override
                        public LocationBean createFromParcel(Parcel source) {
                            return new LocationBean(source);
                        }

                        @Override
                        public LocationBean[] newArray(int size) {
                            return new LocationBean[size];
                        }
                    };
                }

                public static class PicListBean implements Parcelable {

                    private String picUrlSmall;
                    private String picUrl;

                    public String getPicUrlSmall() {
                        return picUrlSmall;
                    }

                    public void setPicUrlSmall(String picUrlSmall) {
                        this.picUrlSmall = picUrlSmall;
                    }

                    public String getPicUrl() {
                        return picUrl;
                    }

                    public void setPicUrl(String picUrl) {
                        this.picUrl = picUrl;
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(this.picUrlSmall);
                        dest.writeString(this.picUrl);
                    }

                    public PicListBean() {
                    }

                    protected PicListBean(Parcel in) {
                        this.picUrlSmall = in.readString();
                        this.picUrl = in.readString();
                    }

                    public static final Creator<PicListBean> CREATOR = new Creator<PicListBean>() {
                        @Override
                        public PicListBean createFromParcel(Parcel source) {
                            return new PicListBean(source);
                        }

                        @Override
                        public PicListBean[] newArray(int size) {
                            return new PicListBean[size];
                        }
                    };
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.proId);
                    dest.writeString(this.summary);
                    dest.writeString(this.cityId);
                    dest.writeParcelable(this.location, flags);
                    dest.writeString(this.cityName);
                    dest.writeString(this.areaId);
                    dest.writeString(this.id);
                    dest.writeString(this.proName);
                    dest.writeString(this.areaName);
                    dest.writeString(this.address);
                    dest.writeString(this.name);
                    dest.writeString(this.star);
                    dest.writeString(this.attention);
                    dest.writeString(this.coupon);
                    dest.writeString(this.opentime);
                    dest.writeString(this.content);
                    dest.writeString(this.price);
                    dest.writeList(this.picList);
                }

                public ContentlistBean() {
                }

                protected ContentlistBean(Parcel in) {
                    this.proId = in.readString();
                    this.summary = in.readString();
                    this.cityId = in.readString();
                    this.location = in.readParcelable(LocationBean.class.getClassLoader());
                    this.cityName = in.readString();
                    this.areaId = in.readString();
                    this.id = in.readString();
                    this.proName = in.readString();
                    this.areaName = in.readString();
                    this.address = in.readString();
                    this.name = in.readString();
                    this.star = in.readString();
                    this.attention = in.readString();
                    this.coupon = in.readString();
                    this.opentime = in.readString();
                    this.content = in.readString();
                    this.price = in.readString();
                    this.picList = new ArrayList<PicListBean>();
                    in.readList(this.picList, PicListBean.class.getClassLoader());
                }

                public static final Creator<ContentlistBean> CREATOR = new Creator<ContentlistBean>() {
                    @Override
                    public ContentlistBean createFromParcel(Parcel source) {
                        return new ContentlistBean(source);
                    }

                    @Override
                    public ContentlistBean[] newArray(int size) {
                        return new ContentlistBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.allPages);
                dest.writeInt(this.currentPage);
                dest.writeInt(this.allNum);
                dest.writeInt(this.maxResult);
                dest.writeList(this.contentlist);
            }

            public PagebeanBean() {
            }

            protected PagebeanBean(Parcel in) {
                this.allPages = in.readInt();
                this.currentPage = in.readInt();
                this.allNum = in.readInt();
                this.maxResult = in.readInt();
                this.contentlist = new ArrayList<ContentlistBean>();
                in.readList(this.contentlist, ContentlistBean.class.getClassLoader());
            }

            public static final Parcelable.Creator<PagebeanBean> CREATOR = new Parcelable.Creator<PagebeanBean>() {
                @Override
                public PagebeanBean createFromParcel(Parcel source) {
                    return new PagebeanBean(source);
                }

                @Override
                public PagebeanBean[] newArray(int size) {
                    return new PagebeanBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.ret_code);
            dest.writeParcelable(this.pagebean, flags);
        }

        public ShowapiResBodyBean() {
        }

        protected ShowapiResBodyBean(Parcel in) {
            this.ret_code = in.readInt();
            this.pagebean = in.readParcelable(PagebeanBean.class.getClassLoader());
        }

        public static final Creator<ShowapiResBodyBean> CREATOR = new Creator<ShowapiResBodyBean>() {
            @Override
            public ShowapiResBodyBean createFromParcel(Parcel source) {
                return new ShowapiResBodyBean(source);
            }

            @Override
            public ShowapiResBodyBean[] newArray(int size) {
                return new ShowapiResBodyBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.showapi_res_code);
        dest.writeString(this.showapi_res_error);
        dest.writeParcelable(this.showapi_res_body, flags);
    }

    public HotSpotsBean() {
    }

    protected HotSpotsBean(Parcel in) {
        this.showapi_res_code = in.readInt();
        this.showapi_res_error = in.readString();
        this.showapi_res_body = in.readParcelable(ShowapiResBodyBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<HotSpotsBean> CREATOR = new Parcelable.Creator<HotSpotsBean>() {
        @Override
        public HotSpotsBean createFromParcel(Parcel source) {
            return new HotSpotsBean(source);
        }

        @Override
        public HotSpotsBean[] newArray(int size) {
            return new HotSpotsBean[size];
        }
    };
}
